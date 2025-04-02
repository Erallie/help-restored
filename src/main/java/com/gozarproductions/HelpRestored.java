package com.gozarproductions;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.java.JavaPlugin;

import com.gozarproductions.commands.HelpCommand;
import com.gozarproductions.commands.HelpTabCompleter;
import com.gozarproductions.managers.UpdateChecker;
import com.gozarproductions.utils.CustomHelpTopic;
import com.gozarproductions.utils.CustomIndexHelpTopic;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class HelpRestored extends JavaPlugin {
    private FileConfiguration helpConfig;
    private HelpMap helpMap;
    private List<CustomIndexHelpTopic> indexTopics;
    private UpdateChecker updateChecker;
    // private HelpCommand helpCommand;

    public FileConfiguration getHelpConfig() {
        return helpConfig;
    }

    public HelpMap getHelpMap() {
        return helpMap;
    }

    public List<CustomIndexHelpTopic> getIndexTopics() {
        return indexTopics;
    }

    @Override
    public void onEnable() {
        getLogger().info("HelpRestored enabled.");
        loadHelpConfig();
        helpMap = Bukkit.getHelpMap();
        indexTopics = new ArrayList<>();

        // helpCommand = new HelpCommand(this);
        getCommand("help").setExecutor(new HelpCommand(this));
        getCommand("help").setTabCompleter(new HelpTabCompleter(this));
        getCommand("helpreload").setExecutor(new HelpReload());

        
        // Run the Update Checker using GitHub API
        updateChecker = new UpdateChecker(this, "Erallie", "help-restored", "helprestored.admin");
        updateChecker.checkForUpdates();
        
        // Delay topic loading to ensure help.yml is fully registered
        Bukkit.getScheduler().runTask(this, () -> loadCustomTopics());
    }

    private void loadHelpConfig() {
        File helpFile = new File(getServer().getWorldContainer(), "help.yml");

        if (!helpFile.exists()) {
            getLogger().warning("help.yml not found in server root.");
            return;
        }

        helpConfig = YamlConfiguration.loadConfiguration(helpFile);
    }

    
    private void loadCustomTopics() {
        if (helpConfig == null) return;

        for (String section : Arrays.asList("general-topics", "amended-topics", "index-topics", "index-topics")) {
            if (!helpConfig.contains(section)) continue;

            ConfigurationSection configSection = helpConfig.getConfigurationSection(section);

            for (String key : configSection.getKeys(false)) {
                String path = section + "." + key;
                String shortText = helpConfig.getString(path + ".shortText", null);
                String permission = helpConfig.getString(path + ".permission", null);
                String fullText = helpConfig.getString(path + ".fullText", null);
                switch (configSection.getName()) {
                    case "general-topics":
                        CustomHelpTopic generalTopic = new CustomHelpTopic(key, shortText, fullText, permission);
                        getLogger().info("Added general topic: " + key);
                        helpMap.addTopic(generalTopic);
                        break;
                    case "amended-topics":
                        HelpTopic ammendedTopic;
                        ammendedTopic = helpMap.getHelpTopic(key);
                        if (ammendedTopic != null) {
                            ammendedTopic.amendTopic(shortText, fullText);
                            ammendedTopic.amendCanSee(permission);
                            getLogger().info("Ammended topic: " + key);
                        }
                        break;
                    case "index-topics":
                        String preamble = helpConfig.getString(path + ".preamble", null);
                        Collection<HelpTopic> subtopics = helpConfig.getStringList(path + ".commands").stream()
                            .map(cmd -> {
                                // First try your custom indexTopics
                                HelpTopic topic = indexTopics.stream()
                                    .filter(t -> t.getName().equalsIgnoreCase(cmd))
                                    .findFirst()
                                    .orElse(null);

                                // If not found, fallback to Bukkit's helpMap
                                return (topic != null) ? topic : helpMap.getHelpTopic(cmd);
                            })
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                        CustomIndexHelpTopic indexTopic = new CustomIndexHelpTopic(key, shortText, permission, preamble, subtopics);
                        Optional<CustomIndexHelpTopic> existing = indexTopics.stream()
                            .filter(t -> t.getName().equals(key))
                            .findFirst();
                        if (existing.isPresent()) {
                            indexTopics.remove(existing.get());
                            getLogger().info("Removed index topic: " + key);
                        }
                        indexTopics.add(indexTopic);
                        getLogger().info("Added index topic: " + key);
                        break;
                }
            }
        }

        /* String section = "index-topics";
        if (!helpConfig.contains(section)) return;

        ConfigurationSection configSection = helpConfig.getConfigurationSection(section);

        for (String key : configSection.getKeys(false)) {
            String path = section + "." + key;
            String shortText = helpConfig.getString(path + ".shortText", null);
            String permission = helpConfig.getString(path + ".permission", null);

            Collection<HelpTopic> subtopics = helpConfig.getStringList(path + ".commands").stream()
                .map(helpMap::getHelpTopic)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
            IndexHelpTopic indexTopic = new IndexHelpTopic(key, shortText, permission, subtopics);
            helpMap.addTopic(indexTopic);
            getLogger().info("Added index topic: " + key);
        } */
    }

    public class HelpReload implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            loadHelpConfig();
            loadCustomTopics();
            
            updateChecker.checkForUpdates();
            sender.sendMessage("§6help.yml §esuccessfully reloaded!");
            return true;
        }
        
    }
}

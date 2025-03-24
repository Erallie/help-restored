package com.gozarproductions;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.help.GenericCommandHelpTopic;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class HelpRestored extends JavaPlugin {
    private FileConfiguration helpConfig;
    private HelpMap helpMap;
    // private HelpCommand helpCommand;

    public FileConfiguration getHelpConfig() {
        return helpConfig;
    }

    public HelpMap getHelpMap() {
        return helpMap;
    }

    @Override
    public void onEnable() {
        getLogger().info("HelpRestored enabled.");
        loadHelpConfig();
        helpMap = Bukkit.getHelpMap();

        // helpCommand = new HelpCommand(this);
        // getCommand("help").setExecutor(helpCommand);
        getCommand("helpreload").setExecutor(new HelpReload());
        
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

        for (String section : Arrays.asList("general-topics", "amended-topics", "index-topics")) {
            if (!helpConfig.contains(section)) continue;

            ConfigurationSection configSection = helpConfig.getConfigurationSection(section);

            for (String key : configSection.getKeys(false)) {
                String path = section + "." + key;
                String shortText = helpConfig.getString(path + ".shortText", null);
                String permission = helpConfig.getString(path + ".permission", null);
                String fullText = helpConfig.getString(path + ".fullText", null);
                switch (configSection.getName()) {
                    case "general-topics":
                        GenericCommandHelpTopic generalTopic = new GenericCommandHelpTopic(Bukkit.getPluginCommand(key));
                        generalTopic.amendTopic(shortText, fullText);
                        generalTopic.amendCanSee(permission);
                        helpMap.addTopic(generalTopic);
                        break;
                    case "amended-topics":
                        HelpTopic ammendedTopic;
                        ammendedTopic = helpMap.getHelpTopic(key);
                        ammendedTopic.amendTopic(shortText, fullText);
                        ammendedTopic.amendCanSee(permission);
                        break;
                    case "index-topics":
                        Collection<HelpTopic> subtopics = helpConfig.getStringList(path + ".commands").stream()
                            .map(helpMap::getHelpTopic)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                        IndexHelpTopic indexTopic = new IndexHelpTopic(key, shortText, permission, subtopics);
                        helpMap.addTopic(indexTopic);
                        break;
                }
            }
        }
    }

    public class HelpReload implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            loadHelpConfig();
            loadCustomTopics();
            sender.sendMessage("§6help.yml §esuccessfully reloaded!");
            return true;
        }
        
    }
}

package com.gozarproductions;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class HelpRestored extends JavaPlugin {

    private String getCustomShortText(String topicName) {
        String cleanName = topicName.startsWith("/") ? topicName.substring(1) : topicName;
        if (helpConfig == null) return null;
        if (helpConfig.contains("general-topics." + topicName + ".shortText"))
            return helpConfig.getString("general-topics." + topicName + ".shortText");
        if (helpConfig.contains("amended-topics." + topicName + ".shortText"))
            return helpConfig.getString("amended-topics." + topicName + ".shortText");
        if (helpConfig.contains("index-topics." + topicName + ".shortText"))
            return helpConfig.getString("index-topics." + topicName + ".shortText");
        if (helpConfig.contains("general-topics." + cleanName + ".shortText"))
            return helpConfig.getString("general-topics." + cleanName + ".shortText");
        if (helpConfig.contains("amended-topics." + cleanName + ".shortText"))
            return helpConfig.getString("amended-topics." + cleanName + ".shortText");
        if (helpConfig.contains("index-topics." + cleanName + ".shortText"))
            return helpConfig.getString("index-topics." + cleanName + ".shortText");
        return null;
    }

    private FileConfiguration helpConfig;
    private HelpMap helpMap;

    @Override
    public void onEnable() {
        getLogger().info("HelpRestored detected.");
        loadHelpConfig();
        helpMap = Bukkit.getHelpMap();
        getCommand("help").setExecutor(new HelpCommand());
        getLogger().info("HelpRestored enabled.");
    }

    private void loadHelpConfig() {
        File helpFile = new File(getServer().getWorldContainer(), "help.yml");
        if (!helpFile.exists()) {
            getLogger().warning("help.yml not found in server root.");
            return;
        }
        helpConfig = YamlConfiguration.loadConfiguration(helpFile);
    }

    public class HelpCommand implements CommandExecutor {
        private static final int ENTRIES_PER_PAGE = 9;

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            int page = 1;
            String query = null;

            if (args.length == 1) {
                try {
                    page = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    query = args[0];
                }
            } else if (args.length >= 2) {
                query = args[0];
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage("§cInvalid page number.");
                    return true;
                }
            }

            if (query != null) {
                HelpTopic topic = helpMap.getHelpTopic(query);
                if (topic != null && topic.canSee(sender)) {
                    String name = topic.getName();
                    List<String> lines;
                    if (helpConfig.contains("general-topics." + name + ".fullText")) {
                        lines = Arrays.asList(helpConfig.getString("general-topics." + name + ".fullText").split("\n"));
                    } else if (helpConfig.contains("amended-topics." + name + ".fullText")) {
                        String full = helpConfig.getString("amended-topics." + name + ".fullText");
                        lines = Arrays.asList(full.replace("<text>", topic.getFullText(sender)).split("\n"));
                    } else if (helpConfig.contains("index-topics." + name + ".preamble")) {
                        lines = new ArrayList<>();
                        lines.addAll(Arrays.asList(helpConfig.getString("index-topics." + name + ".preamble").split("\n")));
                        lines.add(" ");
                        for (String cmd : helpConfig.getStringList("index-topics." + name + ".commands")) {
                            HelpTopic sub = helpMap.getHelpTopic(cmd);
                            String desc = getCustomShortText(cmd);
                            if ((desc == null || desc.isEmpty()) && sub != null) {
                                desc = sub.getShortText();
                            }
                            lines.add("§e" + cmd + " §7- " + (desc != null ? desc : ""));
                        }
                    } else {
                        lines = Arrays.asList(topic.getFullText(sender).split("\n"));
                    }

                    sender.sendMessage("§6--- Help: " + topic.getName() + " ---");
                    int totalPages = (int) Math.ceil((double) lines.size() / ENTRIES_PER_PAGE);
                    if (page > totalPages || page < 1) {
                        sender.sendMessage("§cPage not found. There are only " + totalPages + " pages.");
                        return true;
                    }
                    int start = (page - 1) * ENTRIES_PER_PAGE;
                    int end = Math.min(start + ENTRIES_PER_PAGE, lines.size());
                    for (int i = start; i < end; i++) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lines.get(i)));
                    }
                } else {
                    sender.sendMessage("§cNo help topic found for: " + query);
                }
                return true;
            }

            if (helpConfig.contains("index-topics.Default")) {
                List<String> lines = new ArrayList<>();
                lines.addAll(Arrays.asList(helpConfig.getString("index-topics.Default.preamble", "").split("\n")));
                lines.add(" ");
                for (String cmd : helpConfig.getStringList("index-topics.Default.commands")) {
                    HelpTopic sub = helpMap.getHelpTopic(cmd);
                    String desc = getCustomShortText(cmd);
                    if ((desc == null || desc.isEmpty()) && sub != null) {
                        desc = sub.getShortText();
                    }
                    lines.add("§e" + cmd + " §7- " + (desc != null ? desc : ""));
                }

                int totalPages = (int) Math.ceil((double) lines.size() / ENTRIES_PER_PAGE);
                if (page > totalPages || page < 1) {
                    sender.sendMessage("§cPage not found. There are only " + totalPages + " pages.");
                    return true;
                }

                sender.sendMessage("§6--- Help: Page " + page + " of " + totalPages + " ---");
                int start = (page - 1) * ENTRIES_PER_PAGE;
                int end = Math.min(start + ENTRIES_PER_PAGE, lines.size());
                for (int i = start; i < end; i++) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lines.get(i)));
                }

                return true;
            }

            return true;
        }
    }
}

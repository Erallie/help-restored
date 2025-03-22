package com.gozarproductions;

import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class HelpRestored extends JavaPlugin {

    private final Map<String, HelpTopicData> helpTopics = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("HelpRestored detected.");
        loadHelpTopics();
        getCommand("help").setExecutor(new HelpCommand());
        getLogger().info("HelpRestored enabled.");
    }

    private void loadHelpTopics() {
        getLogger().info("HelpRestored load started...");
        File helpFile = new File(getServer().getWorldContainer(), "help.yml");
        if (!helpFile.exists()) {
            getLogger().warning("help.yml not found in server root.");
            return;
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(helpFile);
        getLogger().info("Loading help topics from help.yml...");

        if (config.isConfigurationSection("general-topics")) {
            for (String key : config.getConfigurationSection("general-topics").getKeys(false)) {
                String path = "general-topics." + key;
                String fullText = config.getString(path + ".fullText", "");
                List<String> content = Arrays.asList(fullText.split("\n"));
                String permission = config.getString(path + ".permission", "");
                helpTopics.put(key.toLowerCase(), new HelpTopicData(key, content, permission));
                getLogger().info("Loaded general topic: " + key);
            }
        }

        if (config.isConfigurationSection("index-topics")) {
            for (String key : config.getConfigurationSection("index-topics").getKeys(false)) {
                String path = "index-topics." + key;
                List<String> commands = config.getStringList(path + ".commands");
                List<String> content = new ArrayList<>();
                String preamble = config.getString(path + ".preamble", "");
                content.addAll(Arrays.asList(preamble.split("\n")));
                content.add(" ");
                for (String cmd : commands) {
                    content.add("§e" + cmd);
                }
                String permission = config.getString(path + ".permission", "");
                helpTopics.put(key.toLowerCase(), new HelpTopicData(key, content, permission));
                getLogger().info("Loaded index topic: " + key);
            }
        }

        if (config.isConfigurationSection("amended-topics")) {
            for (String key : config.getConfigurationSection("amended-topics").getKeys(false)) {
                String path = "amended-topics." + key;
                String fullText = config.getString(path + ".fullText", "<text>");
                List<String> content = Arrays.asList(fullText.split("\n"));
                String permission = config.getString(path + ".permission", "");
                helpTopics.put(key.toLowerCase(), new HelpTopicData(key, content, permission));
                getLogger().info("Loaded amended topic: " + key);
            }
        }

        getLogger().info("Finished loading " + helpTopics.size() + " help topics.");
    }

    public class HelpCommand implements CommandExecutor {
        private static final int ENTRIES_PER_PAGE = 7;

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
                HelpTopicData topic = helpTopics.get(query.toLowerCase());
                if (topic != null && topic.canSee(sender)) {
                    List<String> content = topic.getContent();
                    int totalPages = (int) Math.ceil((double) content.size() / 9);
                    if (page > totalPages || page < 1) {
                        sender.sendMessage("§cPage not found. There are only " + totalPages + " pages.");
                        return true;
                    }
                    sender.sendMessage("§6--- Help: " + topic.getName() + " (Page " + page + " of " + totalPages + ") ---");
                    int start = (page - 1) * 9;
                    int end = Math.min(start + 9, content.size());
                    for (int i = start; i < end; i++) {
                        sender.sendMessage(content.get(i));
                    }
                } else {
                    sender.sendMessage("§cNo help topic found for: " + query);
                }
                return true;
            } else if (helpTopics.containsKey("default")) {
                HelpTopicData defaultTopic = helpTopics.get("default");
                if (defaultTopic.canSee(sender)) {
                    List<String> content = defaultTopic.getContent();
                    int totalPages = (int) Math.ceil((double) content.size() / 10);
                    if (page > totalPages || page < 1) {
                        sender.sendMessage("§cPage not found. There are only " + totalPages + " pages.");
                        return true;
                    }
                    sender.sendMessage("§6--- Help: Default (Page " + page + " of " + totalPages + ") ---");
                    int start = (page - 1) * 10;
                    int end = Math.min(start + 10, content.size());
                    for (int i = start; i < end; i++) {
                        sender.sendMessage(content.get(i));
                    }
                    return true;
                }
            }

            List<HelpTopicData> visibleTopics = new ArrayList<>();
            for (HelpTopicData topic : helpTopics.values()) {
                if (topic.canSee(sender)) visibleTopics.add(topic);
            }

            visibleTopics.sort(Comparator.comparing(HelpTopicData::getName));
            int totalPages = (int) Math.ceil((double) visibleTopics.size() / ENTRIES_PER_PAGE);
            if (page > totalPages || page < 1) {
                sender.sendMessage("§cPage not found. There are only " + totalPages + " pages.");
                return true;
            }

            sender.sendMessage("§6--- Help: Page " + page + " of " + totalPages + " ---");
            int start = (page - 1) * ENTRIES_PER_PAGE;
            int end = Math.min(start + ENTRIES_PER_PAGE, visibleTopics.size());
            for (int i = start; i < end; i++) {
                HelpTopicData topic = visibleTopics.get(i);
                sender.sendMessage("§e/help " + topic.getName() + "§7 - " + topic.getPreview());
            }

            return true;
        }
    }

    public static class HelpTopicData {
        private final String name;
        private final List<String> content;
        private final String permission;

        public HelpTopicData(String name, List<String> content, String permission) {
            this.name = name;
            this.content = content;
            this.permission = permission;
        }

        public boolean canSee(CommandSender sender) {
            return permission.isEmpty() || sender.hasPermission(permission);
        }

        public String getName() {
            return name;
        }

        public List<String> getContent() {
            return content;
        }

        public String getPreview() {
            return content.isEmpty() ? "" : content.get(0);
        }
    }
}

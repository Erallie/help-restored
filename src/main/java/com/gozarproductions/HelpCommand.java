package com.gozarproductions;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;


public class HelpCommand implements CommandExecutor {
    private final HelpRestored plugin;
    private static final int ENTRIES_PER_PAGE = 9;

    public HelpCommand(HelpRestored plugin) {
        this.plugin = plugin;
    }


    private HelpTopic getTopic(String rawName) {
        String commandName = "/" + rawName;

        for (IndexHelpTopic topic : plugin.getIndexTopics()) {
            String topicName = topic.getName();
            if (topicName.equalsIgnoreCase(rawName) || topicName.equalsIgnoreCase(commandName)) {
                return topic;
            }
        }

        // Try case-insensitive match in Bukkit's help map
        for (HelpTopic topic : plugin.getHelpMap().getHelpTopics()) {
            String topicName = topic.getName();
            if (topicName.equalsIgnoreCase(rawName) || topicName.equalsIgnoreCase(commandName)) {
                return topic;
            }
        }

        return null;
    }

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
            HelpTopic topic = getTopic(query);
            if (topic != null && topic.canSee(sender)) {
                List<String> lines = Arrays.asList(topic.getFullText(sender).split("\n"));
                sendPaged(sender, "Help: " + topic.getName(), lines, page);
            } else {
                sender.sendMessage("§cNo help topic found for: " + query);
            }
            return true;
        }

        // Show default index if defined
        HelpTopic defaultTopic = getTopic("Default");
        if (defaultTopic != null && defaultTopic.canSee(sender)) {
            List<String> lines = Arrays.asList(defaultTopic.getFullText(sender).split("\n"));
            sendPaged(sender, "Help: Page " + page, lines, page);
            return true;
        }

        sender.sendMessage("§cNo help topics available.");
        return true;
    }

    private void sendPaged(CommandSender sender, String title, List<String> lines, int page) {
        int totalPages = (int) Math.ceil((double) lines.size() / ENTRIES_PER_PAGE);
        if (page < 1 || page > totalPages) {
            sender.sendMessage("§cPage not found. There are only " + totalPages + " pages.");
            return;
        }

        sender.sendMessage("§6--- " + title + " (" + page + "/" + totalPages + ") ---");
        int start = (page - 1) * ENTRIES_PER_PAGE;
        int end = Math.min(start + ENTRIES_PER_PAGE, lines.size());

        for (int i = start; i < end; i++) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lines.get(i)));
        }
    }
}

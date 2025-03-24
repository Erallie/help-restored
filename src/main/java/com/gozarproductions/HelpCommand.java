package com.gozarproductions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;


public class HelpCommand implements CommandExecutor {
    private final HelpRestored plugin;
    private static final int ENTRIES_PER_PAGE = 10;

    public HelpCommand(HelpRestored plugin) {
        this.plugin = plugin;
    }


    private HelpTopic getTopic(String rawName) {
        String commandName = "/" + rawName;

        for (CustomIndexHelpTopic topic : plugin.getIndexTopics()) {
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
                String preamble = null;
                if (topic instanceof CustomIndexHelpTopic) {
                    preamble = ((CustomIndexHelpTopic) topic).getPreamble();
                }
                sendPaged(sender, "Help: " + topic.getName(), topic.getFullText(sender), preamble, page);
            } else {
                sender.sendMessage("§cNo help topic found for: " + query);
            }
            return true;
        }

        // Show default index if defined
        HelpTopic defaultTopic = getTopic("Default");
        if (defaultTopic != null && defaultTopic.canSee(sender)) {
            String preamble = null;
            if (defaultTopic instanceof CustomIndexHelpTopic) {
                preamble = ((CustomIndexHelpTopic) defaultTopic).getPreamble();
            }
            sendPaged(sender, "Help: Page " + page, defaultTopic.getFullText(sender), preamble, page);
            return true;
        }

        sender.sendMessage("§cNo help topics available.");
        return true;
    }

    private void sendPaged(CommandSender sender, String title, String fullText, String preamble, int page) {
        List<String> preambleLines = new ArrayList<>();
        if (preamble != null){
            preambleLines = Arrays.asList(preamble.split("\n"));
        }
        
        List<String> lines = Arrays.asList(fullText.split("\n"));
        int numEntries = ENTRIES_PER_PAGE - (preambleLines.size() + 1);
        int totalPages = (int) Math.ceil((double) lines.size() / numEntries);
        if (page < 1 || page > totalPages) {
            String verb = "are";
            String noun = "pages";
            if (totalPages == 1) {
                verb = "is";
                noun = "page";
            }
            sender.sendMessage("§cPage not found. There " + verb + " only " + totalPages + " " + noun + ".");
            return;
        }

        sender.sendMessage("§6--- " + title + " (" + page + "/" + totalPages + ") ---");
        for (String line : preambleLines) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }

        int start = (page - 1) * numEntries;
        int end = Math.min(start + numEntries, lines.size());

        for (int i = start; i < end; i++) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lines.get(i)));
        }
    }
}

package com.gozarproductions;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class HelpRestored extends JavaPlugin {

    private HelpMap helpMap;

    @Override
    public void onEnable() {
        this.helpMap = Bukkit.getHelpMap();
        getCommand("help").setExecutor(new HelpCommand());
        getLogger().info("CustomHelpPlugin enabled.");
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

            List<HelpTopic> topics;
            if (query == null) {
                topics = new ArrayList<>(helpMap.getHelpTopics());
            } else {
                HelpTopic topic = helpMap.getHelpTopic(query);
                if (topic != null) {
                    sender.sendMessage(topic.getFullText(sender));
                    return true;
                } else {
                    sender.sendMessage("§cNo help topic found for: " + query);
                    return true;
                }
            }

            topics.removeIf(t -> !t.canSee(sender));
            topics.sort(Comparator.comparing(HelpTopic::getName));

            int totalPages = (int) Math.ceil((double) topics.size() / ENTRIES_PER_PAGE);
            if (page > totalPages || page < 1) {
                sender.sendMessage("§cPage not found. There are only " + totalPages + " pages.");
                return true;
            }

            sender.sendMessage("§6--- Help: Page " + page + " of " + totalPages + " ---");
            int start = (page - 1) * ENTRIES_PER_PAGE;
            int end = Math.min(start + ENTRIES_PER_PAGE, topics.size());
            for (int i = start; i < end; i++) {
                HelpTopic topic = topics.get(i);
                sender.sendMessage("§e" + topic.getName() + "§7 - " + topic.getShortText());
            }

            return true;
        }
    }
}

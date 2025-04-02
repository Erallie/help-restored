package com.gozarproductions.commands;


import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.help.HelpTopic;

import com.gozarproductions.HelpRestored;
import com.gozarproductions.utils.CustomIndexHelpTopic;
import com.gozarproductions.utils.HelpPageInfo;
import com.gozarproductions.commands.HelpCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class HelpTabCompleter implements TabCompleter {
    private final HelpRestored plugin;

    public HelpTabCompleter(HelpRestored plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // Suggest topic names
            String input = args[0].toLowerCase(Locale.ROOT);
            Set<String> suggestions = new HashSet<>();

            if (input.isEmpty()) {
                // Only show index topics when nothing is typed
                for (CustomIndexHelpTopic topic : plugin.getIndexTopics()) {
                    if (topic.canSee(sender)) {
                        suggestions.add(stripLeadingSlash(topic.getName()));
                    }
                }
            } else {
                for (HelpTopic topic : plugin.getHelpMap().getHelpTopics()) {
                    String name = topic.getName();
                    String strippedName = stripLeadingSlash(name);
                    if (topic.canSee(sender)) {
                        if (strippedName.toLowerCase(Locale.ROOT).startsWith(input)) {
                            suggestions.add(strippedName);
                        } else if (name.toLowerCase(Locale.ROOT).startsWith(input)) {
                            suggestions.add(name);
                        }
                    }
                }

                for (CustomIndexHelpTopic topic : plugin.getIndexTopics()) {
                    String name = topic.getName();
                    String strippedName = stripLeadingSlash(name);
                    if (topic.canSee(sender)) {
                        if (strippedName.toLowerCase(Locale.ROOT).startsWith(input)) {
                            suggestions.add(strippedName);
                        } else if (name.toLowerCase(Locale.ROOT).startsWith(input)) {
                            suggestions.add(name);
                        }
                    }
                }
            }

            return suggestions.stream()
                    .sorted(String.CASE_INSENSITIVE_ORDER)
                    .collect(Collectors.toList());

        } else if (args.length == 2) {
            String rawName = args[0];
            String pageInput = args[1];
            HelpTopic topic = null;

            for (CustomIndexHelpTopic indexTopic : plugin.getIndexTopics()) {
                if (indexTopic.getName().equalsIgnoreCase(rawName) || indexTopic.getName().equalsIgnoreCase("/" + rawName)) {
                    topic = indexTopic;
                    break;
                }
            }

            if (topic == null) {
                for (HelpTopic bukkitTopic : plugin.getHelpMap().getHelpTopics()) {
                    if (bukkitTopic.getName().equalsIgnoreCase(rawName) || bukkitTopic.getName().equalsIgnoreCase("/" + rawName)) {
                        topic = bukkitTopic;
                        break;
                    }
                }
            }

            if (topic == null || !topic.canSee(sender)) return Collections.emptyList();

            String fullText = topic.getFullText(sender);
            String preamble = (topic instanceof CustomIndexHelpTopic)
                ? ((CustomIndexHelpTopic) topic).getPreamble()
                : null;

            HelpPageInfo pageInfo = HelpCommand.paginate(fullText, preamble);
            int totalPages = pageInfo.totalPages;

            List<String> result = new ArrayList<>();
            for (int i = 1; i <= totalPages; i++) {
                String pageStr = String.valueOf(i);
                if (pageStr.startsWith(pageInput)) {
                    result.add(pageStr);
                }
            }

            return result;
        }


        return Collections.emptyList();
    }

    private String stripLeadingSlash(String s) {
        return s.startsWith("/") ? s.substring(1) : s;
    }
}



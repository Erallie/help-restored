/* package com.gozarproductions;

import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;

import java.util.List;

public class CustomHelpTopic extends HelpTopic {
    private final String name;
    private final String shortText;
    private final List<String> fullTextLines;
    private final String permission;
    private final HelpTopic fallback;

    public CustomHelpTopic(String name, String shortText, List<String> fullTextLines, String permission, HelpTopic fallback) {
        this.name = name;
        this.shortText = shortText;
        this.fullTextLines = fullTextLines;
        this.permission = permission;
        this.fallback = fallback;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getShortText() {
        return (shortText != null && !shortText.isEmpty())
                ? shortText
                : (fallback != null ? fallback.getShortText() : "");
    }

    @Override
    public String getFullText(CommandSender sender) {
        if (fullTextLines != null && !fullTextLines.isEmpty()) {
            if (fullTextLines.size() == 1 && fullTextLines.get(0).contains("<text>") && fallback != null) {
                return fullTextLines.get(0).replace("<text>", fallback.getFullText(sender));
            }
            return String.join("\n", fullTextLines);
        }
        return fallback != null ? fallback.getFullText(sender) : "§cHelp content not found.";
    }


    @Override
    public boolean canSee(CommandSender sender) {
        if (permission != null && !permission.isEmpty()) {
            return sender.hasPermission(permission);
        } else if (fallback != null) {
            return fallback.canSee(sender);
        } else {
            return true; // default to visible
        }
    }


    public int compareTo(HelpTopic other) {
        return this.getName().compareToIgnoreCase(other.getName());
    }
}
 */
package com.gozarproductions;

import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpTopic;

public class CustomHelpTopic extends HelpTopic {
    private final String name;
    private final String shortText;
    private final String fullText;
    private final String permission;

    public CustomHelpTopic(String name, String shortText, String fullText, String permission) {
        this.name = name;
        this.shortText = shortText;
        this.fullText = fullText;
        this.permission = permission;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getShortText() {
        return shortText;
    }

    @Override
    public String getFullText(CommandSender sender) {
        return fullText;
    }


    @Override
    public boolean canSee(CommandSender sender) {
        if (permission != null && !permission.isEmpty()) {
            return sender.hasPermission(permission);
        } else {
            return true; // default to visible
        }
    }
}

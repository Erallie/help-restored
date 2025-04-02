package com.gozarproductions.utils;

import org.bukkit.help.HelpTopic;
import org.bukkit.help.IndexHelpTopic;

import java.util.Collection;

public class CustomIndexHelpTopic extends IndexHelpTopic {
    private final String preamble;

    public CustomIndexHelpTopic(String name, String shortText, String permission, String preamble, Collection<HelpTopic> topics) {
        super(name, shortText, permission, topics);
        if (preamble != null) {
            this.preamble = preamble;
        } else {
            this.preamble = "Use /help <command> for more info.";
        }
    }

    public String getPreamble() {
        return preamble;
    }
}

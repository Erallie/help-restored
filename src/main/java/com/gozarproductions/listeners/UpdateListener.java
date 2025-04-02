package com.gozarproductions.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.gozarproductions.HelpRestored;


public class UpdateListener implements Listener {
    private final HelpRestored plugin;
    private final String notifyPermission;

    public UpdateListener(HelpRestored plugin, String notifyPermission) {
        this.plugin = plugin;
        this.notifyPermission = notifyPermission;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Check if the player is an admin
        if (player.hasPermission(notifyPermission)) {
            // Check if an update is available
            plugin.getUpdateChecker().recallAndNotify(player);
        }
    }
}

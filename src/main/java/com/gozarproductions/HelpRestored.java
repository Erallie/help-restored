package com.gozarproductions;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

// Main plugin class that extends JavaPlugin (the entry point for Bukkit plugins)
public class HelpRestored extends JavaPlugin {

    

    private FileConfiguration helpConfig; // Loaded help.yml file
    private HelpMap helpMap;              // Bukkit's built-in help topic registry

    public HelpMap getHelpMap() {
        return helpMap;
    }

    public FileConfiguration getHelpConfig() {
        return helpConfig;
    }
    // Called when the plugin is enabled
    @Override
    public void onEnable() {
        getLogger().info("HelpRestored detected.");
        loadHelpConfig(); // Load the help.yml file
        helpMap = Bukkit.getHelpMap(); // Reference to Bukkit's help system
        getCommand("help").setExecutor(new HelpCommand(this)); // Register /help command
        getLogger().info("HelpRestored enabled.");
    }

    // Loads the help.yml file from the server root directory
    private void loadHelpConfig() {
        File helpFile = new File(getServer().getWorldContainer(), "help.yml");

        if (!helpFile.exists()) {
            getLogger().warning("help.yml not found in server root.");
            return;
        }

        helpConfig = YamlConfiguration.loadConfiguration(helpFile);
    }
}

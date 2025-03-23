package com.gozarproductions;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class HelpRestored extends JavaPlugin {
    private FileConfiguration helpConfig;
    private HelpMap helpMap;

    public FileConfiguration getHelpConfig() {
        return helpConfig;
    }

    public HelpMap getHelpMap() {
        return helpMap;
    }

    @Override
    public void onEnable() {
        getLogger().info("HelpRestored enabled.");
        loadHelpConfig();
        this.helpMap = Bukkit.getHelpMap();

        PluginCommand helpCommand = getCommand("help");
        if (helpCommand != null) {
            helpCommand.setExecutor(new HelpCommand(this));
        }
    }

    private void loadHelpConfig() {
        File helpFile = new File(getServer().getWorldContainer(), "help.yml");

        if (!helpFile.exists()) {
            getLogger().warning("help.yml not found in server root.");
            return;
        }

        helpConfig = YamlConfiguration.loadConfiguration(helpFile);
    }
}

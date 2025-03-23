package com.gozarproductions;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.help.HelpMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class HelpRestored extends JavaPlugin {
    private FileConfiguration helpConfig;
    private HelpMap helpMap;
    private HelpCommand helpCommand;

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
        helpMap = Bukkit.getHelpMap();

        helpCommand = new HelpCommand(this);
        getCommand("help").setExecutor(helpCommand);
        getCommand("helpreload").setExecutor(new HelpReload());
        
        // Delay topic loading to ensure help.yml is fully registered
        Bukkit.getScheduler().runTask(this, () -> helpCommand.reload());
    }

    private void loadHelpConfig() {
        File helpFile = new File(getServer().getWorldContainer(), "help.yml");

        if (!helpFile.exists()) {
            getLogger().warning("help.yml not found in server root.");
            return;
        }

        helpConfig = YamlConfiguration.loadConfiguration(helpFile);
    }

    public class HelpReload implements CommandExecutor {
        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            loadHelpConfig();
            helpCommand.reload();
            return true;
        }
        
    }
}

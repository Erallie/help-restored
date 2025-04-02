package com.gozarproductions.managers;

import com.gozarproductions.HelpRestored;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ConfigUpdater {
    private final HelpRestored plugin;

    public ConfigUpdater(HelpRestored plugin) {
        this.plugin = plugin;
    }

    public void checkAndUpdateConfigs() {
        String currentVersion = plugin.getDescription().getVersion();
        String storedVersion = plugin.getConfig().getString("internal.plugin-version", "0.0");

        if (!currentVersion.equals(storedVersion)) {
            plugin.getLogger().info("Detected new version (" + currentVersion + "). Updating configuration files...");

            updateYamlFile("config.yml");

            plugin.reloadConfig();
            plugin.getConfig().set("internal.plugin-version", currentVersion);
            plugin.saveConfig();
        }
    }

    private void updateYamlFile(String fileName) {
        File file = new File(plugin.getDataFolder(), fileName);
        if (!file.exists()) return;

        try {
            FileConfiguration userConfig = YamlConfiguration.loadConfiguration(file);
            InputStream defaultStream = plugin.getResource(fileName);
            if (defaultStream == null) return;

            // Step 1: Load entire default config as text
            StringBuilder defaultText = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(defaultStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    defaultText.append(line).append("\n");
                }
            }

            // Step 2: Write the raw default file to disk
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(defaultText.toString());
            }

            // Step 3: Reload written default config
            FileConfiguration newConfig = YamlConfiguration.loadConfiguration(file);

            // Step 4: Set all user-defined keys back into the config
            for (String key : userConfig.getKeys(true)) {
                Object value = userConfig.get(key);
                if (value != null) {
                    newConfig.set(key, value);
                }
            }

            // Step 5: Save final config with merged values
            newConfig.save(file);
            plugin.getLogger().info("Updated " + fileName + " by restoring user values into the new default config.");

        } catch (IOException e) {
            plugin.getLogger().severe("Could not update " + fileName + ": " + e.getLocalizedMessage());
        }
    }
}

package org.btc.features.modules;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class ResourceFileManager {

    public static void ensureResourceFilesExist(JavaPlugin plugin) {
        // Define the resource files and their locations
        String[] resourceFiles = {
                "modules/WelcomeModule.yml",
                "modules/ChatClear.yml",
                "modules/ChatFiltration.yml",
                "modules/ChatMute.yml",
                "modules/ServerListMOTD.yml",
                "modules/ServerResourcePack.yml",
                "modules/PluginCmdBlock.yml",
                "modules/Maintenance.yml"
                // Add other resources here as needed
        };

        // Iterate through each resource file
        for (String resourceFile : resourceFiles) {
            File file = new File(plugin.getDataFolder(), resourceFile);

            // If the file doesn't exist, create it
            if (!file.exists()) {
                try {
                    file.getParentFile().mkdirs();  // Create any missing directories
                    plugin.saveResource(resourceFile, false); // Save the resource from the plugin jar
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                    // You can add additional default YAML configuration if needed
                    config.set("enabled", true); // Example: Set 'enabled' to true by default
                    config.save(file); // Save the configuration to the file
                    plugin.getLogger().info("Created and saved file: " + resourceFile);
                } catch (IOException e) {
                    plugin.getLogger().warning("Failed to create or save the file: " + resourceFile);
                    e.printStackTrace();
                }
            } else {
                plugin.getLogger().info("File already exists: " + resourceFile);
            }
        }
    }
}

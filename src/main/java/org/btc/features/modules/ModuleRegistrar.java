package org.btc.features.modules;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class ModuleRegistrar {

    public static void registerAll(JavaPlugin plugin, ModuleManager manager) {
        File configDir = new File(plugin.getDataFolder(), "modules");

        if (!configDir.exists()) {
            configDir.mkdirs();
            plugin.saveResource("modules/WelcomeModule.yml", false); // Add default if needed
        }

        File[] files = configDir.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;

        for (File file : files) {
            String moduleName = file.getName().replace(".yml", "");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            boolean enabled = config.getBoolean("enabled", true);
            if (!enabled) {
                plugin.getLogger().info("Module " + moduleName + " is disabled.");
                continue;
            }

            try {
                Class<?> clazz = Class.forName("org.btc.features.modules." + moduleName);
                if (!Module.class.isAssignableFrom(clazz)) {
                    plugin.getLogger().warning("Class " + moduleName + " does not implement Module, skipping.");
                    continue;
                }
                Module module = (Module) clazz.getConstructor(JavaPlugin.class).newInstance(plugin);
                manager.registerModule(module);
                plugin.getLogger().info("Loaded module: " + moduleName);
            } catch (ClassNotFoundException e) {
                plugin.getLogger().warning("No class found for module: " + moduleName);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to load module: " + moduleName);
                e.printStackTrace();
            }
        }
    }
}

package org.btc.features.modules;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class AbstractModule implements Module {

    private final String name;
    private final JavaPlugin plugin;
    private boolean enabled;
    private File configFile;
    private FileConfiguration config;

    public AbstractModule(String name, JavaPlugin plugin) {
        this.name = name;
        this.plugin = plugin;
        loadOrCreateConfig();
    }

    /**
     * Loads or creates the module's config file.
     * Merges defaults from the JAR if available.
     */
    private void loadOrCreateConfig() {
        File folder = new File(plugin.getDataFolder(), "modules");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        this.configFile = new File(folder, name + ".yml");

        if (!configFile.exists()) {
            InputStream jarResource = plugin.getResource("modules/" + name + ".yml");
            if (jarResource != null) {
                plugin.saveResource("modules/" + name + ".yml", false);
                plugin.getLogger().info("Created default config for module: " + name);
            } else {
                try {
                    if (configFile.createNewFile()) {
                        plugin.getLogger().info("Created empty config for module: " + name);
                    }
                } catch (IOException e) {
                    plugin.getLogger().warning("Failed to create config for module: " + name);
                    e.printStackTrace();
                }
            }
        }

        this.config = YamlConfiguration.loadConfiguration(configFile);

        // Merge defaults if a JAR version exists
        InputStream defaultsStream = plugin.getResource("modules/" + name + ".yml");
        if (defaultsStream != null) {
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultsStream));
            config.setDefaults(defaults);
            config.options().copyDefaults(true);
            saveConfig();
        }
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save config for module: " + name);
            e.printStackTrace();
        }
    }

    protected void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    protected void unregisterAllListeners() {
        HandlerList.unregisterAll(plugin);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) onEnable();
        else onDisable();
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }
}

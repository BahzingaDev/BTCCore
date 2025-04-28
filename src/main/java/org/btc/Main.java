package org.btc;

import org.btc.api.BTCCoreExpansion;
import org.btc.features.ServerCommand;
import org.btc.features.ServerConnector;
import org.btc.features.modules.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;
public class Main extends JavaPlugin {

    public Logger logger;
    public String ver, apiVer, author;

    private ModuleManager moduleManager;
    private ServerConnector serverConnector;

    @Override
    public void onEnable() {

        this.serverConnector = new ServerConnector(this);
        getCommand("btcserver").setExecutor(new ServerCommand(this));

        // Ensure all required resource files are created if they don't already exist
        ResourceFileManager.ensureResourceFilesExist(this);

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new BTCCoreExpansion(this).register();
            getLogger().info("PlaceholderAPI hooked successfully.");
        } else {
            getLogger().warning("PlaceholderAPI not found — skipping placeholder support.");
        }

        // Initialize logger and plugin metadata AFTER plugin is constructed
        logger = getLogger();
        PluginDescriptionFile pdf = getDescription();
        ver = pdf.getVersion();
        apiVer = pdf.getAPIVersion();
        author = pdf.getAuthors().get(0);

        // Init module system
        this.moduleManager = new ModuleManager();

        // Register modules
        ModuleRegistrar.registerAll(this, moduleManager);

        moduleManager.enableAll();

        // Register command
        getCommand("modules").setExecutor(new ModuleCommand(this, moduleManager));

        logger.info("Starting BTC Core version " + ver + " Minecraft " + apiVer + " by " + author);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("btccore.admin")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&6&lBTC&f&lMC &8&m|&r &7Started core plugin for Minecraft &9" + apiVer + "&7."));
            }
        }
    }

    @Override
    public void onDisable() {
        if (moduleManager != null){
            moduleManager.disableAll();
        }

        if (serverConnector != null) {
            serverConnector.shutdown();
        }
    }

    /** Expose your connector to the rest of the plugin */
    public ServerConnector getServerConnector() {
        return this.serverConnector;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}

package org.btc.features.modules;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerListMOTD extends AbstractModule {

    public ServerListMOTD(JavaPlugin plugin) {
        super("ServerListMOTD", plugin);
    }

    @Override
    public void onEnable() {
        // Register event listeners
        reloadConfig(); //
        registerListener(new ServerListListener());

        // Register commands
//        getPlugin().getCommand("example").setExecutor(new ExampleCommand());

        // Admin notification
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("btccore.admin")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&6&lBTC&f&lMC &8&m|&r &7Enabled module '" + getName() + "'."));
            }
        }
    }

    @Override
    public void onDisable() {
        unregisterAllListeners();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("btccore.admin")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&6&lBTC&f&lMC &8&m|&r &7Disabled module '" + getName() + "'."));
            }
        }
    }

    // ----------------------------
    // Internal event listener
    // ----------------------------
    private class ServerListListener implements Listener {

        @EventHandler
        public void onPing(ServerListPingEvent event){

            String motd = getConfig().getString("motd", "&6&lBTC&f&lMC &8&m|&r &7Welcome to our server!");
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                motd = PlaceholderAPI.setPlaceholders(null, motd);
            }
            event.setMotd(ChatColor.translateAlternateColorCodes('&', motd));

        }
    }

    // ----------------------------
    // Internal command executor
    // ----------------------------
//    private class ExampleCommand implements CommandExecutor {
//
//        @Override
//        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//            if (!sender.hasPermission("btccore.example.use")) {
//                sender.sendMessage(MessageUtil.color("&6&lBTC&f&lMC &8&m|&r &cYou don't have permission to use this command."));
//                return true;
//            }
//
//            sender.sendMessage(MessageUtil.color("&6&lBTC&f&lMC &8&m|&r &aExample command executed successfully!"));
//            return true;
//        }
//    }
}

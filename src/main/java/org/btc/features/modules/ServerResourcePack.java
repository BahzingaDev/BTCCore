package org.btc.features.modules;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerResourcePack extends AbstractModule {

    public ServerResourcePack(JavaPlugin plugin) {
        super("ServerResourcePack", plugin); // Use the exact name of your module config file (without .yml)
    }

    @Override
    public void onEnable() {
        // Load config
        reloadConfig();

        // Register any listeners
//        registerListener(new ModuleListener());

        // Optional: register commands here
        // getPlugin().getCommand("example").setExecutor(new ExampleCommand());

        // Notify admins
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
    // Event Listener (Optional)
    // ----------------------------
    private class ModuleListener implements Listener {




    }

    // ----------------------------
    // Command Executor (Optional)
    // ----------------------------
//    private class ExampleCommand implements CommandExecutor {
//
//        @Override
//        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//            if (!sender.hasPermission("btccore.example.use")) {
//                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
//                return true;
//            }
//            sender.sendMessage(ChatColor.GREEN + "Example command executed.");
//            return true;
//        }
//    }
}

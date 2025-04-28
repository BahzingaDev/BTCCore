package org.btc.features.modules;

import org.btc.messages.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginCmdBlock extends AbstractModule {

    public PluginCmdBlock(JavaPlugin plugin) {
        super("PluginCmdBlock", plugin);
    }

    @Override
    public void onEnable() {
        // Register event listeners
        registerListener(new ModuleListener());

        // Register commands
//        getPlugin().getCommand("clearchat").setExecutor(new ChatClearCmd());

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
    private class ModuleListener implements Listener {

        @EventHandler
        public void onCommand(PlayerCommandPreprocessEvent event){

            String command = event.getMessage().toLowerCase();

            // Commands to block
            if (command.equals("/pl") || command.equals("/plugins") || command.equals("/?")) {

                String cmdPerm = getConfig().getString("plugins-perm");

                if (!event.getPlayer().hasPermission(cmdPerm) && !event.getPlayer().isOp()) {
                    event.setCancelled(true); // Block the command
                    for (Player p : Bukkit.getOnlinePlayers()){
                        if (p.hasPermission("btccore.admin")){
                            p.sendMessage(
                                    MessageUtil.format("&7Player &c" + event.getPlayer().getName() + " &7attempted to run admin command: &c" + command)
                            );
                        }
                    }
                }
            }

        }

    }

    // ----------------------------
    // Internal command executor
    // ----------------------------
//    private class ChatClearCmd implements CommandExecutor {
//
//
//    }
}

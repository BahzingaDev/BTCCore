package org.btc.features.modules;

import org.btc.messages.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Maintenance extends AbstractModule {

    public Maintenance(JavaPlugin plugin) {
        super("Maintenance", plugin);
    }

    @Override
    public void onEnable() {
        // Register event listeners
        registerListener(new ModuleListener());

        // Register commands
        getPlugin().getCommand("maintenance").setExecutor(new ModuleCmd());

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
        public void onJoin(PlayerJoinEvent event){

            boolean maintenance = getConfig().getBoolean("maintenance.enabled");
            if (maintenance){
                Player player = event.getPlayer();
                if (!player.hasPermission("btccore.admin")){
                    player.kickPlayer(MessageUtil.format("&cThis server is now under maintenance.\n&cPlease try again later."));
                }
            }

        }


    }

    // ----------------------------
    // Internal command executor
    // ----------------------------
    private class ModuleCmd implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if (label.equalsIgnoreCase("maintenance")){
                if (sender instanceof Player){

                    Player player = (Player) sender;
                    if (player.hasPermission("btccore.admin")){

                        boolean maintenance = getConfig().getBoolean("maintenance.enabled");
                        if (maintenance){
                            getConfig().set("maintenance.enabled", false);

                            for (Player p : Bukkit.getOnlinePlayers()){
                                p.sendMessage(MessageUtil.format(getConfig().getString("maintenance.message.disabled")));
                            }

                        } else {
                            getConfig().set("maintenance.enabled", true);

                            for (Player p : Bukkit.getOnlinePlayers()){
                                p.sendMessage(MessageUtil.format(getConfig().getString("maintenance.message.enabled")));

                                if (!p.hasPermission("btccore.admin")){
                                    p.kickPlayer(MessageUtil.format("&cThis server is now under maintenance."));
                                }

                            }

                        }
                        saveConfig();

                    }

                }
            }

            return true;
        }
    }
}

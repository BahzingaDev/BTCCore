package org.btc.features.modules;

import org.btc.messages.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class WelcomeModule extends AbstractModule {

    public WelcomeModule(JavaPlugin plugin){
        super("WelcomeModule", plugin);
    }

    @Override
    public void onEnable() {
        registerListener(new WelcomeListener());

        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.hasPermission("btccore.admin")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&6&lBTC&f&lMC &8&m|&r &7Enabled module '" + getName() + "'."));
            }
        }
    }

    @Override
    public void onDisable() {
        unregisterAllListeners();

        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.hasPermission("btccore.admin")){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&6&lBTC&f&lMC &8&m|&r &7Disabled module '" + getName() + "'."));
            }
        }
    }

    private class WelcomeListener implements Listener {

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();

            // Welcome message
            List<String> msg = getConfig().getStringList("join-msg");

            for (int i = 0;i<196;i++){
                player.sendMessage(" ");
            }

            for (String m : msg){
                player.sendMessage(MessageUtil.format(player, m));
            }

            // Announce join
            boolean announceEnabled = getConfig().getBoolean("announce-join.enabled");
            String announceMsg = getConfig().getString("announce-join.message");
            String sound = getConfig().getString("announce-join.sound");

            event.setJoinMessage("");
            if (announceEnabled){
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(MessageUtil.format(player, announceMsg));
                    if (sound != null) {
                        players.playSound(players, Sound.valueOf(sound), 10, 10);
                    }
                }
            }
        }

        @EventHandler
        public void onLeave(PlayerQuitEvent event){

            Player player = event.getPlayer();

            if (getConfig().getBoolean("announce-leave.enabled")){

                String announceMsg = getConfig().getString("announce-leave.message");
                String sound = getConfig().getString("announce-leave.sound");

                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.sendMessage(MessageUtil.format(player, announceMsg));
                    if (sound != null) {
                        players.playSound(players, Sound.valueOf(sound), 10, 10);
                    }
                }
            }

        }
    }
}

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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatMute extends AbstractModule {

    public ChatMute(JavaPlugin plugin) {
        super("ChatMute", plugin);
    }

    @Override
    public void onEnable() {
        // Register event listeners
        registerListener(new ChatListener());

        // Register commands
        getPlugin().getCommand("mutechat").setExecutor(new MuteChatCmd());

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
    private class ChatListener implements Listener {

        @EventHandler
        public void onChat(AsyncPlayerChatEvent event){

            Player player = event.getPlayer();
            boolean chatMuted = getConfig().getBoolean("chatmute.enabled");
            String messageBlock = getConfig().getString("chatmute.block");
            String bypassPerm = getConfig().getString("chatmute.bypass-perm");

            if (chatMuted){

                if (!player.hasPermission(bypassPerm)){
                    player.sendMessage(MessageUtil.format("&6&lBTC&f&lMC &8&m|&r " + messageBlock));
                    event.setCancelled(true);
                }

            }


        }
    }

    // ----------------------------
    // Internal command executor
    // ----------------------------
    private class MuteChatCmd implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if (label.equalsIgnoreCase("mutechat")){
                if (sender instanceof Player){

                    Player player = (Player) sender;
                    if (player.hasPermission("btccore.chat.mute")){

                        boolean chatMuted = getConfig().getBoolean("chatmute.enabled");
                        String chatMutedMsg = getConfig().getString("chatmute.muted-message");
                        String chatUnmutedMsg = getConfig().getString("chatmute.unmuted-message");

                        if (chatMuted){
                            getConfig().set("chatmute.enabled", false);
                            saveConfig();

                            for (Player players : Bukkit.getOnlinePlayers()){
                                players.sendMessage(MessageUtil.format(
                                        chatUnmutedMsg
                                ));
                            }

                        } else {
                            getConfig().set("chatmute.enabled", true);
                            saveConfig();

                            for (Player players : Bukkit.getOnlinePlayers()){
                                players.sendMessage(MessageUtil.format(
                                        chatMutedMsg
                                ));
                            }

                        }


                    } else {
                        player.sendMessage(MessageUtil.format(
                                "&6&lBTC&f&lMC &8&m|&r &cYou don't have access to that command!"
                        ));
                    }

                }
            }

            return true;
        }
    }
}

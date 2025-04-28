package org.btc.features.modules;

import org.btc.messages.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatClear extends AbstractModule {

    public ChatClear(JavaPlugin plugin) {
        super("ChatClear", plugin);
    }

    @Override
    public void onEnable() {
        // Register event listeners
//        registerListener(new ChatListener());

        // Register commands
        getPlugin().getCommand("clearchat").setExecutor(new ChatClearCmd());

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


    }

    // ----------------------------
    // Internal command executor
    // ----------------------------
    private class ChatClearCmd implements CommandExecutor {

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

            if (label.equalsIgnoreCase("clearchat")){
                if (sender instanceof Player){

                    Player player = (Player) sender;
                    if (player.hasPermission("btccore.chat.clear")){

                        String clearChatMsg = getConfig().getString("chat-clear.message");

                        for (Player p : Bukkit.getOnlinePlayers()){
                            for (int i = 0;i<196;i++){
                                p.sendMessage(" ");
                            }
                            p.sendMessage(MessageUtil.format(clearChatMsg));
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

package org.btc.features.modules;

import org.btc.messages.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

public class ChatFiltration extends AbstractModule {

    private List<String> blacklist;
    private List<Pattern> regexList;
    private Map<UUID, Long> lastMessageTimestamps;
    private int spamThresholdMillis;
    private String bypassPermission;
    private String warnMessage;
    private boolean logToConsole;


    public ChatFiltration(JavaPlugin plugin){
        super("ChatFiltration", plugin);
    }

    @Override
    public void onEnable() {

        loadConfigValues();
        registerListener(new ChatFilterListener());
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

    private void loadConfigValues() {
        FileConfiguration config = getConfig();

        blacklist = config.getStringList("chat-filter.blacklist");
        regexList = new ArrayList<>();
        for (String regex : config.getStringList("chat-filter.regex")) {
            regexList.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
        }

        spamThresholdMillis = config.getInt("chat-filter.spam-threshold", 1500);
        bypassPermission = config.getString("chat-filter.bypass-permission", "btccore.chat.bypass");
        warnMessage = config.getString("chat-filter.warn-message", "&6&lBTC&f&lMC &8&m|&r &cPlease do not send inappropriate or spam messages.");
        logToConsole = config.getBoolean("chat-filter.log-to-console", true);
    }




    private class ChatFilterListener implements Listener{

        @EventHandler
        public void onChat(AsyncPlayerChatEvent event) {
            Player player = event.getPlayer();
            String message = event.getMessage();

            if (player.hasPermission(bypassPermission)) return;

            // Spam protection
            long now = System.currentTimeMillis();
            long last = lastMessageTimestamps.getOrDefault(player.getUniqueId(), 0L);
            if (now - last < spamThresholdMillis) {
                event.setCancelled(true);
                player.sendMessage(MessageUtil.format(player, warnMessage));
                if (logToConsole) {
                    Bukkit.getLogger().warning("[BTCCore - Chat Filtration] " + player.getName() + " blocked for spam: " + message);
                }
                return;
            }
            lastMessageTimestamps.put(player.getUniqueId(), now);

            // Blacklist check
            for (String blocked : blacklist) {
                if (message.toLowerCase().contains(blocked.toLowerCase())) {
                    event.setCancelled(true);
                    player.sendMessage(MessageUtil.format(player, warnMessage));
                    if (logToConsole) {
                        Bukkit.getLogger().warning("[BTCCore - Chat Filtration] " + player.getName() + " blocked for blacklisted word: " + message);
                    }
                    return;
                }
            }

            // Regex check
            for (Pattern pattern : regexList) {
                if (pattern.matcher(message).find()) {
                    event.setCancelled(true);
                    player.sendMessage(MessageUtil.format(player, warnMessage));
                    if (logToConsole) {
                        Bukkit.getLogger().warning("[ChatFilter] " + player.getName() + " blocked by regex: " + message);
                    }
                    return;
                }
            }
        }

    }

}

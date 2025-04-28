package org.btc.messages;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageUtil {

    private static final String PREFIX = "&6&lBTC&f&lMC &8&m|&r&7 ";

    /**
     * Formats a message with the standard prefix and color codes.
     *
     * @param message The message to format.
     * @return The formatted message.
     */
    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', PREFIX + message);
    }

    /**
     * Formats a message with the standard prefix, color codes, and PlaceholderAPI placeholders.
     *
     * @param player  The player for whom to parse placeholders.
     * @param message The message to format.
     * @return The formatted message with placeholders parsed.
     */
    public static String format(Player player, String message) {
        // Replace placeholders
        String parsed = PlaceholderAPI.setPlaceholders(player, message);
        // Translate color codes
        return ChatColor.translateAlternateColorCodes('&', parsed);
    }

}

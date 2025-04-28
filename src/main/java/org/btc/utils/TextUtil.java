package org.btc.utils;

import org.bukkit.ChatColor;

public class TextUtil {

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

}

package me.shreyasayyengar.tnttag.utils;

import org.bukkit.ChatColor;

public class Utility {

    public static String colourise(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}

package me.shreyasayyengar.tnttag.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Utility {

    public static String colourise(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static ItemStack air() {
        return new ItemStack(Material.AIR);
    }
}

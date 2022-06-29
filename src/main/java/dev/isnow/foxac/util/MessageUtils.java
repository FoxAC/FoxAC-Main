package dev.isnow.foxac.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author 5170
 * made on dev.isnow.foxac.util
 */

@UtilityClass
public class MessageUtils {

    public String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public void sendConsoleMessage(String input) {
        Bukkit.getConsoleSender().sendMessage(color("&7------------------------"));
        Bukkit.getConsoleSender().sendMessage(color("&c[FOXAC] &7- " + input));
    }
}

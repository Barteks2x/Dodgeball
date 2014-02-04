package com.github.barteks2x.dodgeball;

import org.bukkit.ChatColor;
import static org.bukkit.Color.*;
import org.bukkit.Color;

public enum DodgeballTeam {

    WHITE(ChatColor.WHITE, Color.WHITE),
    ORANGE(ChatColor.YELLOW, Color.ORANGE),
    MAGENTA(ChatColor.DARK_AQUA, Color.fromRGB(255, 0, 255)),
    LIGHT_BLUE(ChatColor.AQUA, Color.AQUA),
    YELLOW(ChatColor.YELLOW, Color.YELLOW),
    LIME(ChatColor.GREEN, Color.LIME),
    PINK(ChatColor.RED, Color.fromRGB(255, 204, 221)),
    GRAY(ChatColor.GRAY, Color.GRAY),
    LIGHT_GRAY(ChatColor.GRAY, Color.fromRGB(180, 180, 180)),
    CYAN(ChatColor.RED, Color.fromRGB(0, 255, 255)),
    PURPLE(ChatColor.LIGHT_PURPLE, Color.fromRGB(212,111, 249)),
    BLUE(ChatColor.BLUE, Color.BLUE),
    BROWN(ChatColor.DARK_RED, Color.fromRGB(128, 64, 0)),
    GREEN(ChatColor.GREEN, Color.GREEN),
    RED(ChatColor.RED, Color.RED),
    BLACK(ChatColor.BLACK, Color.BLACK);
    private final ChatColor color;
    private final Color rgb;

    private DodgeballTeam(ChatColor color, Color RGBColor) {
        this.color = color;
        this.rgb = RGBColor;
    }

    ChatColor getChatColor() {
        return color;
    }
    
    Color getRGBColor(){
        return this.rgb;
    }
}

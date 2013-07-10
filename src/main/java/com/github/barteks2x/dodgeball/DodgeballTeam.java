package com.github.barteks2x.dodgeball;

import org.bukkit.ChatColor;

public enum DodgeballTeam {

	WHITE(ChatColor.WHITE), ORANGE(ChatColor.YELLOW), MAGENTA(ChatColor.DARK_AQUA), LIGHT_BLUE(
	ChatColor.AQUA), YELLOW(ChatColor.YELLOW), LIME(ChatColor.GREEN), PINK(ChatColor.RED), GRAY(
	ChatColor.GRAY), LIGHT_GRAY(ChatColor.GRAY), CYAN(ChatColor.RED), PURPLE(ChatColor.LIGHT_PURPLE),
	BLUE(ChatColor.BLUE),
	BROWN(ChatColor.DARK_RED), GREEN(ChatColor.GREEN), RED(ChatColor.RED), BLACK(ChatColor.BLACK);
	private ChatColor color;

	private DodgeballTeam(ChatColor color) {
		this.color = color;
	}

	ChatColor getColor() {
		return color;
	}
}

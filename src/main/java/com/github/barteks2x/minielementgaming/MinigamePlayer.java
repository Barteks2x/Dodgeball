package com.github.barteks2x.minielementgaming;

import com.github.barteks2x.minielementgaming.minigames.Minigame;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MinigamePlayer {

	private final MinigameTeam team;
	private final Player player;
	public int health;
	private double spawnX;
	private final Minigame m;

	public MinigamePlayer(Player player, MinigameTeam team, Minigame mg) {

		this.player = player;
		this.team = team;
		this.m = mg;
		player.setHealth(6);
		player.setFoodLevel(4);
		player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short)team.ordinal()));
		health = 6;
		spawnX = mg.getSpawnX(this);
	}

	public MinigameTeam getTeam() {
		return team;
	}

	public Player getPlayer() {
		return player;
	}

	public Minigame getMinigame() {
		return m;
	}

	public String playerName() {
		return player.getName();
	}

	public double getSpawnX() {
		return spawnX;
	}

	public void setSpawnX(double x) {
		this.spawnX = x;
	}
}

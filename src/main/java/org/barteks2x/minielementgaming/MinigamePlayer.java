package org.barteks2x.minielementgaming;

import org.bukkit.entity.Player;

public class MinigamePlayer {

	private final MinigameTeam team;
	private final Player player;
	private final MinigameEnum mg;
	public int health;
	private double spawnX;

	public MinigamePlayer(Player player, MinigameTeam team, MinigameEnum mg) {
		this.player = player;
		this.team = team;
		this.mg = mg;
	}

	public MinigameTeam getTeam() {
		return team;
	}

	public Player getPlayer() {
		return player;
	}

	public MinigameEnum getMinigame() {
		return mg;
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

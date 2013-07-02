package org.barteks2x.minielementgaming;

import org.bukkit.entity.Player;

public class MinigamePlayer {

	private final MinigameTeam team;
	private final Player player;
	private final MinigameEnum mg;

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
}

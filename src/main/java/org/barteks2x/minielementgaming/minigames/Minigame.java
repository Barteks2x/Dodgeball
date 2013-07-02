package org.barteks2x.minielementgaming.minigames;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.barteks2x.minielementgaming.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class Minigame implements Serializable {

	private static final long serialVersionUID = 342134832462L;
	protected final CubeSerializable area;
	protected final MinigameEnum minigame;
	protected final transient Map<String, MinigamePlayer> players;
	protected final transient int[] teamPlayerCount;

	protected Minigame(Location minPoint, Location maxPoint, MinigameEnum mg, byte teams) {
		if (teams <= 0) {
			throw new IllegalArgumentException("Zreo or less teams!");
		}
		if (minPoint.equals(maxPoint)) {
			throw new IllegalArgumentException("Arena size is zero!");
		}
		players = new HashMap<String, MinigamePlayer>();
		this.area = new CubeSerializable(minPoint, maxPoint);
		this.minigame = mg;
		teamPlayerCount = new int[teams];
	}

	public abstract void handlePlayerMove(PlayerMoveEvent e);

	public abstract void handlePlayerInteract(PlayerInteractEvent e);

	public abstract void handleEntityDamageByEntity(EntityDamageByEntityEvent e);

	public abstract void handleProjectileHitEvent(ProjectileHitEvent e);

	public void addPlayer(Player player, MinigameTeam team) {
		if (players.containsKey(player.getName())) {
			return;
		}
		players.put(player.getName(), new MinigamePlayer(player, team, minigame));
	}

	public void addPlayer(Player player) {
		if (players.containsKey(player.getName())) {
			return;
		}
		players.put(player.getName(), new MinigamePlayer(player, autoSelectTeam(), minigame));
	}

	public boolean isPlayerInArena(Player p) {
		return area.isInArea(p.getLocation());
	}

	public boolean isPlayerInMinigame(Player p) {
		return players.containsKey(p.getName());
	}

	public void removePlayer(Player player) {
		players.remove(player.getName());
	}

	private MinigameTeam autoSelectTeam() {
		int lowest = Integer.MAX_VALUE;
		int team = 0;
		for (int i = 0; i < teamPlayerCount.length; ++i) {
			if (teamPlayerCount[i] < lowest) {
				team = i;
			}
		}
		return MinigameTeam.values()[team];
	}

	public abstract void onStart();
}

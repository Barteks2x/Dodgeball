package org.barteks2x.minielementgaming;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class ArenaBase implements Serializable {

	private static final long serialVersionUID = 342134832462L;
	protected final LocationSerializable minPoint, maxPoint;
	protected final Minigame minigame;
	protected final Map<String, MinigamePlayer> players;
	protected final int[] teamPlayerCount;

	protected ArenaBase(Location minPoint, Location maxPoint, Minigame mg, byte teams) {
		if (teams <= 0) {
			throw new IllegalArgumentException("Zreo or less teams!");
		}
		if (minPoint.equals(maxPoint)) {
			throw new IllegalArgumentException("Arena size is zero!");
		}
		players = new HashMap<String, MinigamePlayer>();
		this.minPoint = new LocationSerializable(minPoint);
		this.maxPoint = new LocationSerializable(maxPoint);
		this.minigame = mg;
		teamPlayerCount = new int[teams];
	}

	public abstract void handlePlayerMove(PlayerMoveEvent e);

	public abstract void handlePlayerInteract(PlayerInteractEvent e);

	public void addPlayer(Player player, MinigameTeam team) {
	}
}

package com.github.barteks2x.minielementgaming.minigames;

import com.github.barteks2x.minielementgaming.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class Minigame implements Serializable {

	private static final long serialVersionUID = 342134832462L;
	public transient final List<MinigamePlayer> players = new ArrayList<MinigamePlayer>();
	protected final CubeSerializable area;
	protected final MinigameEnum minigame;
	protected final transient int[] teamPlayerCount;
	protected final int[] teamIdMap;
	protected LocationSerializable spawn;
	protected final String name;
	protected final Plugin plug;
	protected MinigameManager mm;

	protected Minigame(Plugin plug, Location minPoint, Location maxPoint, MinigameEnum mg,
			byte teams,
			String name) {
		if (teams <= 0) {
			throw new IllegalArgumentException("Zreo or less teams!");
		}
		if (minPoint.equals(maxPoint)) {
			throw new IllegalArgumentException("Arena size is zero!");
		}
		this.area = new CubeSerializable(minPoint, maxPoint);
		this.minigame = mg;
		teamPlayerCount = new int[teams];
		teamIdMap = new int[teams];
		this.name = name;
		this.plug = plug;
		this.mm = plug.getMinigameManager();
		mm.addMinigame(this);
	}

	public abstract void handlePlayerMove(PlayerMoveEvent e);

	public abstract void handlePlayerInteract(PlayerInteractEvent e);

	public abstract void handleEntityDamageByEntity(EntityDamageByEntityEvent e);

	public abstract void handleProjectileHitEvent(ProjectileHitEvent e);

	public MinigameTeam autoSelectTeam() {
		int lowest = Integer.MAX_VALUE;
		int team = 0;
		for (int i = 0; i < teamPlayerCount.length; ++i) {
			if (teamPlayerCount[i] < lowest) {
				team = i;
				lowest = teamPlayerCount[i];
			}
		}
		teamPlayerCount[team] = lowest + 1;
		return MinigameTeam.values()[teamIdMap[team]];
	}

	public void setSpawn(Location loc) {
		this.spawn = new LocationSerializable(loc);
	}

	public Location getSpawn() {
		return spawn.getLocation();
	}

	public String getName() {
		return this.name;
	}

	public abstract void onStart();

	public abstract double getSpawnX(MinigamePlayer p);
}
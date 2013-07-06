package com.github.barteks2x.dodgeball;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public abstract class Minigame implements Serializable {

	private static final long serialVersionUID = 342134832462L;
	public transient final List<DodgeballPlayer> playerList = new ArrayList<DodgeballPlayer>(10);
	protected final CubeSerializable area;
	protected final MinigameEnum minigame;
	public final transient int[] teamPlayerCount = new int[DodgeballTeam.values().length];
	protected LocationSerializable spawn;
	protected final String name;
	protected final Plugin plug;
	protected MinigameManager mm;
	public int players;
	public boolean isStarted = false;
	public int maxPlayers;
	public int votes;

	protected Minigame(Plugin plug, Location minPoint, Location maxPoint, MinigameEnum mg,
			String name) {
		if (minPoint.equals(maxPoint)) {
			throw new IllegalArgumentException("Arena size is zero!");
		}
		this.area = new CubeSerializable(minPoint, maxPoint);
		this.minigame = mg;
		this.name = name;
		this.plug = plug;
		this.mm = plug.getMinigameManager();
		mm.addMinigame(this);
	}

	public abstract void handlePlayerMove(PlayerMoveEvent e);

	public abstract void handlePlayerInteract(PlayerInteractEvent e);

	public abstract void handleEntityDamageByEntity(EntityDamageByEntityEvent e);

	public abstract void handleProjectileHitEvent(ProjectileHitEvent e);

	public abstract DodgeballTeam autoSelectTeam();

	public void setSpawn(Location loc) {
		this.spawn = new LocationSerializable(loc);
	}

	public Location getSpawn() {
		if (spawn == null) {
			return null;
		}
		return spawn.getLocation();
	}

	public String getName() {
		return this.name;
	}

	public abstract void onStart();

	public abstract void onStop();

	public abstract double getSpawnX(DodgeballPlayer p);

	public abstract boolean hasTeam(String team);

	protected abstract void setPlayerAtRandomLocation(Player player);
}

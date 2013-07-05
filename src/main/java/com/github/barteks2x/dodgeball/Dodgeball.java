package com.github.barteks2x.dodgeball;

import java.util.Random;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Dodgeball extends Minigame {

	private static final long serialVersionUID = 3612989452554623L;
	private final CubeSerializable TEAM_1_AREA;
	private final CubeSerializable TEAM_2_AREA;
	private final CubeSerializable SPECTATE_AREA;
	private final DodgeballTeam TEAM_1, TEAM_2;
	private final transient Random rand = new Random();
	private final double TEAM_1_SPAWN_X, TEAM_2_SPAWN_X;

	public Dodgeball(Plugin plug, Location minPoint, Location maxPoint, String name,
			DodgeballTeam team1, DodgeballTeam team2) {
		super(plug, minPoint, maxPoint, MinigameEnum.DB, name);
		World w = minPoint.getWorld();
		double minX1 = minPoint.getX();
		double minY1 = minPoint.getY();
		double minZ1 = minPoint.getZ();
		double maxX1 = (minPoint.getX() + maxPoint.getX()) / 2;
		double maxY1 = maxPoint.getY();
		double maxZ1 = maxPoint.getZ();
		TEAM_1_AREA = new CubeSerializable(new LocationSerializable(w, minX1, minY1, minZ1),
				new LocationSerializable(w, maxX1, maxY1, maxZ1));
		double minX2 = maxX1;
		double minY2 = minY1;
		double minZ2 = minZ1;
		double maxX2 = maxPoint.getX();
		double maxY2 = maxY1;
		double maxZ2 = maxZ1;
		TEAM_2_AREA = new CubeSerializable(new LocationSerializable(w, minX2, minY2, minZ2),
				new LocationSerializable(w, maxX2, maxY2, maxZ2));
		this.TEAM_1 = team1;
		this.TEAM_2 = team2;
		TEAM_1_SPAWN_X = minX1 + 1;
		TEAM_2_SPAWN_X = maxX2 - 1;
		SPECTATE_AREA = new CubeSerializable(new LocationSerializable(minPoint).add(new Vector(0, 5,
				0)).getLocation(), maxPoint);
	}

	@Override
	public void handlePlayerMove(PlayerMoveEvent e) {
		DodgeballPlayer p = mm.getMinigamePlayer(e.getPlayer().getName());
		p.update(mm, getPlayerTeamArea(p), SPECTATE_AREA, e.getTo());
	}

	@Override
	public boolean hasTeam(String team) {
		if (TEAM_1.toString().equals(team) || TEAM_2.toString().equals(team)) {
			return true;
		}
		return false;
	}

	private CubeSerializable getPlayerTeamArea(DodgeballPlayer p) {
		if (p.getTeam() == TEAM_1) {
			return TEAM_1_AREA;
		} else {
			return TEAM_2_AREA;
		}
	}

	@Override
	public void handlePlayerInteract(PlayerInteractEvent e) {
		//TODO Dodgeball player interact
	}

	@Override
	public void handleEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Snowball s = (Snowball)e.getDamager();
		Player player = (Player)e.getEntity();
		Player shooter = (Player)s.getShooter();
		World w = s.getWorld();
		if (!mm.hasPlayer(player.getName()) || !mm.hasPlayer(shooter.getName())) {
			return;
		}
		if (player.getName().equals(shooter.getName())) {
			e.setCancelled(true);
			return;
		}
		DodgeballPlayer mp = mm.getMinigamePlayer(player.getName());
		DodgeballPlayer ms = mm.getMinigamePlayer(shooter.getName());
		if (mp.getTeam() == ms.getTeam()) {
			e.setCancelled(true);
			return;
		}
		Location itemPos = player.getLocation();
		w.dropItemNaturally(itemPos.add(0, 1, 0), new ItemStack(Material.SNOW_BALL, 1));
		setPlayerAtRandomLocation(player);

		mp.health -= 2;
		mp.update(mm, getPlayerTeamArea(mp), SPECTATE_AREA, player.getLocation());
	}

	@Override
	public void onStart() {
		DodgeballPlayer parray[] = new DodgeballPlayer[1];
		parray = playerList.toArray(parray);
		Player p = (parray[rand.nextInt(parray.length)]).getPlayer();
		p.setItemInHand(new ItemStack(Material.SNOW_BALL, 1));
	}

	@Override
	public void handleProjectileHitEvent(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Snowball) {
			Snowball s = (Snowball)e.getEntity();
			if (area.isInArea(s.getLocation())) {
				s.getWorld().dropItem(s.getLocation(), new ItemStack(Material.SNOW_BALL, 1));
				s.remove();
			}
		}
	}

	@Override
	public double getSpawnX(DodgeballPlayer p) {
		DodgeballTeam t = p.getTeam();
		return t == TEAM_1 ? TEAM_1_SPAWN_X : TEAM_2_SPAWN_X;
	}

	private void setPlayerAtRandomLocation(Player player) {
		String pname = player.getName();
		DodgeballPlayer p = mm.getMinigamePlayer(pname);
		DodgeballTeam t = p.getTeam();
		CubeSerializable a = t == TEAM_1 ? TEAM_1_AREA : TEAM_2_AREA;
		LocationSerializable maxl = a.maxPoint;
		LocationSerializable minl = a.minPoint;
		Location l = player.getLocation();
		l.setX(p.getSpawnX());
		l.setY(minl.y + 2);
		l.setZ(rand.nextInt((int)(maxl.z - minl.z - 2)) + minl.z + 1);
		player.teleport(l);
	}

	@Override
	public DodgeballTeam autoSelectTeam() {
		if (teamPlayerCount[TEAM_1.ordinal()] > teamPlayerCount[TEAM_2.ordinal()]) {
			teamPlayerCount[TEAM_2.ordinal()] += 1;
			return DodgeballTeam.values()[TEAM_1.ordinal()];
		}
		if (teamPlayerCount[TEAM_1.ordinal()] < teamPlayerCount[TEAM_2.ordinal()]) {
			teamPlayerCount[TEAM_1.ordinal()] += 1;
			return DodgeballTeam.values()[TEAM_1.ordinal()];
		}

		return rand.nextBoolean() ? DodgeballTeam.values()[TEAM_1.ordinal()] :
				DodgeballTeam.values()[TEAM_2.ordinal()];
	}
}

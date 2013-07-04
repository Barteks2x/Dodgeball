package com.github.barteks2x.dodgeball;

import com.github.barteks2x.dodgeball.DodgeballTeam;
import com.github.barteks2x.dodgeball.LocationSerializable;
import com.github.barteks2x.dodgeball.Plugin;
import com.github.barteks2x.dodgeball.MinigameEnum;
import com.github.barteks2x.dodgeball.CubeSerializable;
import com.github.barteks2x.dodgeball.DodgeballPlayer;
import java.util.Random;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class Dodgeball extends Minigame {

	private static final long serialVersionUID = 3612989452554623L;
	private final CubeSerializable TEAM_1_AREA;
	private final CubeSerializable TEAM_2_AREA;
	private final DodgeballTeam TEAM_1, TEAM_2;
	private final transient Random rand = new Random();
	private final double TEAM_1_SPAWN_X, TEAM_2_SPAWN_X;

	public Dodgeball(Plugin plug, Location minPoint, Location maxPoint, String name,
			DodgeballTeam team1,
			DodgeballTeam team2) {
		super(plug, minPoint, maxPoint, MinigameEnum.DB, (byte)2, name);
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
		teamIdMap[0] = team1.ordinal();
		teamIdMap[1] = team2.ordinal();
		TEAM_1_SPAWN_X = minX1 + 1;
		TEAM_2_SPAWN_X = maxX2 - 1;
	}

	@Override
	public void handlePlayerMove(PlayerMoveEvent e) {
		DodgeballPlayer p = mm.getMinigamePlayer(e.getPlayer().getName());
		CubeSerializable ca;
		if (p.getTeam() == TEAM_1) {
			ca = TEAM_1_AREA;
		} else {
			ca = TEAM_2_AREA;
		}
		ca.setPlayerInArea(e.getPlayer(), e.getTo());
		e.getPlayer().setFoodLevel(10);
		e.getPlayer().setHealth(p.health >= 0 ? p.health : 0);
		if (p.health <= 0) {
			mm.removePlayer(p);
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
		if (mm.getMinigamePlayer(player.getName()).getTeam() == mm.getMinigamePlayer(shooter.
				getName()).
				getTeam()) {
			e.setCancelled(true);
			return;
		}
		Location itemPos = player.getLocation();
		w.dropItemNaturally(itemPos.add(0, 1, 0), new ItemStack(Material.SNOW_BALL, 1));
		setPlayerAtRandomLocation(player);
		mm.getMinigamePlayer(player.getName()).health -= 2;
	}

	@Override
	public void onStart() {
		DodgeballPlayer parray[] = new DodgeballPlayer[1];
		parray = players.toArray(parray);
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
		if (teamPlayerCount[0] > teamPlayerCount[1]) {
			teamPlayerCount[1] += 1;
			return DodgeballTeam.values()[teamIdMap[1]];
		}
		if (teamPlayerCount[0] < teamPlayerCount[1]) {
			teamPlayerCount[0] += 1;
			return DodgeballTeam.values()[teamIdMap[0]];
		}

		return rand.nextBoolean() ? DodgeballTeam.values()[teamIdMap[0]] :
				DodgeballTeam.values()[teamIdMap[1]];
	}
}

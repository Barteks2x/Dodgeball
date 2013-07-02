package org.barteks2x.minielementgaming.minigames;

import java.util.Random;
import org.barteks2x.minielementgaming.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

public class MinigameDodgeball extends Minigame {

	private static final long serialVersionUID = 3612989452554623L;
	private final CubeSerializable TEAM_1_AREA;
	private final CubeSerializable TEAM_2_AREA;
	public final String NAME;
	private final MinigameTeam TEAM_1, TEAM_2;

	public MinigameDodgeball(Location minPoint, Location maxPoint, String name, MinigameTeam team1,
			MinigameTeam team2) {
		super(minPoint, maxPoint, MinigameEnum.DB, (byte)2);
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
		this.NAME = name;
		this.TEAM_1 = team1;
		this.TEAM_2 = team2;
	}

	@Override
	public void handlePlayerMove(PlayerMoveEvent e) {
		if (!isPlayerInArena(e.getPlayer())) {
			//TODO move player to arena if is outside
		}
		MinigamePlayer p = players.get(e.getPlayer().getName());
		CubeSerializable ca;
		if (p.getTeam() == TEAM_1) {
			ca = TEAM_1_AREA;
		} else {
			ca = TEAM_2_AREA;
		}
		ca.setPlayerInArea(e.getPlayer(), e.getTo());
		e.getPlayer().setFoodLevel(4);
	}

	@Override
	public void handlePlayerInteract(PlayerInteractEvent e) {
		//TODO Dodgeball player interact
	}

	@Override
	public void handleEntityDamageByEntity(EntityDamageByEntityEvent e) {
		Snowball s = (Snowball)e.getEntity();
		Player player = (Player)e.getDamager();
		Player shooter = (Player)s.getShooter();
		if (player.getName().equals(shooter.getName())) {
			e.setCancelled(true);
			return;
		}
		if (players.get(player.getName()).getTeam() == players.get(shooter.getName()).getTeam()) {
			e.setCancelled(true);
			return;
		}
		player.setHealth(player.getHealth() - 2);
		Location itemPos = player.getLocation();
		setPlayerAtRandomLocation(player);
		World w = s.getWorld();
		w.dropItemNaturally(itemPos.add(0, 1, 0), new ItemStack(Material.SNOW_BALL, 1));
	}

	@Override
	public void addPlayer(Player p) {
		super.addPlayer(p);
		p.setHealth(6);
		p.setFoodLevel(4);
		p.setMaxHealth(6);
	}

	@Override
	public void onStart() {
		MinigamePlayer parray[] = (MinigamePlayer[])players.values().toArray();
		Random rand = new Random();
		Player p = parray[rand.nextInt(parray.length)].getPlayer();
		p.setItemInHand(new ItemStack(Material.SNOW_BALL, 1));
	}

	@Override
	public void handleProjectileHitEvent(ProjectileHitEvent e) {
		if (e.getEntity() instanceof Snowball) {
			Snowball s = (Snowball)e.getEntity();
			if (area.isInArea(s.getLocation())) {
				s.getWorld().dropItem(s.getLocation(), new ItemStack(Material.SNOW_BALL));
				s.remove();
			}
		}
	}

	private void setPlayerAtRandomLocation(Player player) {
		//TODO Auto-generated method
	}
}

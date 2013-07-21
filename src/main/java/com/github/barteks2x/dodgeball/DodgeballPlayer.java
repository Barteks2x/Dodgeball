package com.github.barteks2x.dodgeball;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DodgeballPlayer {

	private final double playerHealthUnit;
	private final DodgeballTeam team;
	private final String pName;
	private transient Player player;
	public int health;
	private double spawnX;
	private final Dodgeball m;
	public boolean isSpectator = false;

	public DodgeballPlayer(Player player, DodgeballTeam team, Dodgeball mg) {
		this.pName = player.getName();
		this.player = player;
		this.team = team;
		this.m = mg;
		this.playerHealthUnit = player.getHealth() / 10D;
		player.setHealth(3 * playerHealthUnit);
		player.setFoodLevel(10);
		player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short)team.ordinal()));
		health = 3;
		spawnX = mg.getSpawnX(this);
	}

	public DodgeballTeam getTeam() {
		return team;
	}

	public Player getPlayer() {
		if (player == null) {
			player = Plugin.plug.getServer().getPlayer(pName);
		}
		return player;

	}

	public Dodgeball getMinigame() {
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

	public void update(DodgeballManager mm, CubeSerializable ta, CubeSerializable sa,
			Location newLoc) {
		if (health <= 0) {
			this.health = 10;
			player.setAllowFlight(true);
			player.setFlying(true);
			player.addPotionEffect(
					new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
			player.getInventory().clear();
			PlayerInventory pi = player.getInventory();
			pi.setBoots(null);
			pi.setChestplate(null);
			pi.setHelmet(null);
			pi.setLeggings(null);
			m.onPlayerDeath(this);
		}
		if (isSpectator) {
			player.setAllowFlight(true);
			player.setFlying(true);
			this.health = 10;
			sa.setPlayerInArea(player, newLoc);
		} else {
			ta.setPlayerInArea(player, newLoc);
		}
		player.setHealth(Math.max(0, health) * playerHealthUnit);
		player.setFoodLevel(10);
	}
}

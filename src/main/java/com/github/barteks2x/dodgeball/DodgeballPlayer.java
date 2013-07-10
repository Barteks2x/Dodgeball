package com.github.barteks2x.dodgeball;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DodgeballPlayer {

	private final DodgeballTeam team;
	private final Player player;
	public int health;
	private double spawnX;
	private final Dodgeball m;
	public boolean isSpectator = false;
	private boolean updated = false;

	public DodgeballPlayer(Player player, DodgeballTeam team, Dodgeball mg) {

		this.player = player;
		this.team = team;
		this.m = mg;
		player.setHealth(6);
		player.setFoodLevel(4);
		player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short)team.ordinal()));
		health = 6;
		spawnX = mg.getSpawnX(this);
	}

	public DodgeballTeam getTeam() {
		return team;
	}

	public Player getPlayer() {
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

	public void update(DodgeballManager mm, CubeSerializable ta, CubeSerializable sa, Location newLoc) {
		if ((isSpectator && !updated) || health <= 0) {
			mm.setPlayerSpactate(this);
			this.health = 20;
			player.setAllowFlight(true);
			player.setFlying(true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 1));
			player.getInventory().clear();
			PlayerInventory pi = player.getInventory();
			pi.setBoots(null);
			pi.setChestplate(null);
			pi.setHelmet(null);
			pi.setLeggings(null);
			updated = true;
		}
		player.setHealth(Math.max(0, health));
		player.setFoodLevel(10);
		if (isSpectator) {
			player.setAllowFlight(true);
			player.setFlying(true);
			sa.setPlayerInArea(player, newLoc);
		} else {
			ta.setPlayerInArea(player, newLoc);
		}
	}
}

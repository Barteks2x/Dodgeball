package com.github.barteks2x.dodgeball;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class PlayerData {

	private final Player p;
	private final ItemStack[] inventory;
	private final int health;
	private final float exp;
	private final ItemStack[] armor;
	private final float exhausion;
	private final int food;
	private final int maxHealth;
	private final Location loc;
	private final Minigame m;

	public PlayerData(DodgeballPlayer mp) {
		this.p = mp.getPlayer();
		this.inventory = p.getInventory().getContents();
		this.health = p.getHealth();
		this.exp = p.getExp();
		this.armor = p.getEquipment().getArmorContents();
		this.exhausion = p.getExhaustion();
		this.food = p.getFoodLevel();
		this.maxHealth = p.getMaxHealth();
		this.loc = p.getLocation();
		this.m = mp.getMinigame();
	}

	public void restorePlayerData() {
		p.getInventory().setContents(inventory);
		p.setExp(exp);
		p.getEquipment().setArmorContents(armor);
		p.setExhaustion(exhausion);
		p.setFoodLevel(20);
		p.setMaxHealth(maxHealth);
		p.setHealth(p.getMaxHealth());
		if (m.getSpawn() != null) {
			p.teleport(m.getSpawn());
		} else {
			p.teleport(loc);
		}
	}
}

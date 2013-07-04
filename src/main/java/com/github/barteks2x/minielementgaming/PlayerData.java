package com.github.barteks2x.minielementgaming;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class PlayerData {

	private Player p;
	private ItemStack[] inventory;
	private int health;
	private float exp;
	private ItemStack[] armor;
	private float exhausion;
	private int food;
	private int maxHealth;

	public PlayerData(Player p) {
		this.p = p;
		this.inventory = p.getInventory().getContents();
		this.health = p.getHealth();
		this.exp = p.getExp();
		this.armor = p.getEquipment().getArmorContents();
		this.exhausion = p.getExhaustion();
		this.food = p.getFoodLevel();
		this.maxHealth = p.getMaxHealth();
	}

	public void restorePlayerData() {
		p.getInventory().setContents(inventory);
		p.setExp(exp);
		p.getEquipment().setArmorContents(armor);
		p.setExhaustion(exhausion);
		p.setFoodLevel(food);
		p.setMaxHealth(maxHealth);
		p.setHealth(health);
	}
}

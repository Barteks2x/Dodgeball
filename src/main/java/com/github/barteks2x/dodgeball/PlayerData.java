package com.github.barteks2x.dodgeball;

import java.util.Collection;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

class PlayerData {

	private final Player p;
	private final ItemStack[] inventory;
	private final int health;
	private final float exp;
	private final ItemStack boots, leggins, chestplate, helmet;
	private final float exhausion;
	private final int food;
	private final int maxHealth;
	private final Location loc;
	private final Minigame m;
	private final Collection<PotionEffect> potions;

	public PlayerData(DodgeballPlayer mp) {
		this.p = mp.getPlayer();
		this.inventory = p.getInventory().getContents();
		this.health = p.getHealth();
		this.exp = p.getExp();
		this.helmet = p.getEquipment().getHelmet();
		this.chestplate = p.getEquipment().getChestplate();
		this.leggins = p.getEquipment().getLeggings();
		this.boots = p.getEquipment().getBoots();
		this.exhausion = p.getExhaustion();
		this.food = p.getFoodLevel();
		this.maxHealth = p.getMaxHealth();
		this.loc = p.getLocation();
		this.m = mp.getMinigame();
		this.potions = p.getActivePotionEffects();
	}

	public void restorePlayerData() {
		p.getInventory().setContents(inventory);
		p.setExp(exp);
		p.getEquipment().setBoots(boots);
		p.getEquipment().setLeggings(leggins);
		p.getEquipment().setChestplate(chestplate);
		p.getEquipment().setHelmet(helmet);
		p.setExhaustion(exhausion);
		p.setFoodLevel(20);
		p.setMaxHealth(maxHealth);
		p.setHealth(p.getMaxHealth());
		p.removePotionEffect(PotionEffectType.INVISIBILITY);
		p.addPotionEffects(potions);
		if (m.getSpawn() != null) {
			p.teleport(m.getSpawn());
		} else if (p.getBedSpawnLocation() != null) {
			p.teleport(p.getBedSpawnLocation());
		} else if (loc.getWorld().getSpawnLocation() != null) {
			p.teleport(loc.getWorld().getSpawnLocation());
		} else {
			p.teleport(loc);
		}
	}
}

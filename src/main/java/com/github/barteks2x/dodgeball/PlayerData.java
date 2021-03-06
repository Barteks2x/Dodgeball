package com.github.barteks2x.dodgeball;

import java.util.Collection;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

class PlayerData {

    private final Player p;
    private final ItemStack[] inventory;
    private final float exp;
    private final ItemStack boots, leggins, chestplate, helmet;
    private final float exhausion;
    private final Location loc;
    private final Dodgeball m;
    private final Collection<PotionEffect> potions;
    private final GameMode gm;
    private final boolean fly;
    private final boolean flyAllowed;

    PlayerData(DodgeballPlayer mp) {
        this.p = mp.getPlayer();
        this.inventory = p.getInventory().getContents();
        this.exp = p.getExp();
        this.helmet = p.getInventory().getHelmet();
        this.chestplate = p.getInventory().getChestplate();
        this.leggins = p.getInventory().getLeggings();
        this.boots = p.getInventory().getBoots();
        this.exhausion = p.getExhaustion();
        this.loc = p.getLocation();
        this.m = mp.getMinigame();
        this.potions = p.getActivePotionEffects();
        this.gm = p.getGameMode();
        this.fly = p.isFlying();
        this.flyAllowed = p.getAllowFlight();
    }

    public void restorePlayerData() {
        p.getEquipment().clear();//clear equipment, without this it doesn't remove items
        p.getInventory().setContents(inventory);
        p.setExp(exp);
        p.getInventory().setBoots(boots);
        p.getInventory().setLeggings(leggins);
        p.getInventory().setChestplate(chestplate);
        p.getInventory().setHelmet(helmet);
        p.setExhaustion(exhausion);
        p.setFoodLevel(20);
        p.setHealth(p.getMaxHealth());
        p.removePotionEffect(PotionEffectType.INVISIBILITY);
        p.addPotionEffects(potions);
        p.setGameMode(gm);
        p.setAllowFlight(flyAllowed);
        p.setFlying(fly);
        if(m.getSpawn() != null) {
            p.teleport(m.getSpawn().add(0, 1, 0));//Teleport player to dodgeball spawn
        } else if(m.mm.getGlobalSpawn() != null && m.mm.getGlobalSpawn().getWorldObj() != null) {
            p.teleport(m.mm.getGlobalSpawn().getLocation().add(0, 1, 0));//teleport player to dodgeball global spawn if exists
        } else {
            p.teleport(loc);//teleport player to saved location
        }
    }
}

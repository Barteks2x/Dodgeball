package com.github.barteks2x.dodgeball;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DodgeballPlayer {
    public int health;
    public boolean isSpectator = false;

    private final double playerHealthUnit;
    private final DodgeballTeam team;
    private final String pName;
    private transient Player player;
    private double spawnX;
    private final Dodgeball m;
    private boolean isInit;

    public DodgeballPlayer(Player player, DodgeballTeam team, Dodgeball mg) {
        this.pName = player.getName();
        this.player = player;
        this.team = team;
        this.m = mg;
        this.playerHealthUnit = player.getMaxHealth() / 10D;
        //player.setHealth(3 * playerHealthUnit);
        //player.setFoodLevel(10);
        //player.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short)team.ordinal()));
        health = 3;
        spawnX = mg.getSpawnX(this);
        this.isInit = false;
    }

    public DodgeballTeam getTeam() {
        return team;
    }

    public Player getPlayer() {
        if(player == null) {
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

    public void update(DodgeballManager mm, CubeSerializable ta, CubeSerializable sa, Location newLoc) {
        if(health <= 0) {
            this.health = 10;
            player.setAllowFlight(true);
            player.setFlying(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
            player.getInventory().clear();
            player.getEquipment().clear();
            m.onPlayerDeath(this);
        }
        if(isSpectator) {
            player.setAllowFlight(true);
            player.setFlying(true);
            this.health = 10;
            sa.setPlayerInArea(player, newLoc);
        } else {
            ta.setPlayerInArea(player, newLoc);
        }
        player.setHealth(Math.max(0, health) * playerHealthUnit);
        player.setFoodLevel(10);
        if(!this.isInit) {
            this.init();
            this.isInit = true;
        }
        this.isInit = false;
    }

    private void init() {
        //set correct colors
        ItemStack[] items = new ItemStack[4];
        Material[] materials = new Material[] {Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS};
        for(int i = 0; i < 4; ++i) {
            EntityEquipment equipment = player.getEquipment();
            items[i] = new ItemStack(materials[i], 1);
            LeatherArmorMeta metadata = (LeatherArmorMeta)items[i].getItemMeta();
            Color color = metadata.getColor();
            if(!color.equals(team.getRGBColor())) {
                metadata.setColor(team.getRGBColor());
                items[i].setItemMeta(metadata);
            }
            if(i == 0) {
                equipment.setHelmet(items[i]);
            } else if(i == 1) {
                equipment.setChestplate(items[i]);
            } else if(i == 2) {
                equipment.setLeggings(items[i]);
            } else if(i == 3) {
                equipment.setBoots(items[i]);
            }
        }

    }
}

package com.github.barteks2x.dodgeball;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import static org.bukkit.event.inventory.InventoryType.SlotType.*;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Dodgeball implements Serializable {

    private static final long serialVersionUID = 342134832462L;
    public transient ArrayList<DodgeballPlayer> playerList
            = new ArrayList<DodgeballPlayer>(10);
    public transient int players;
    public int maxPlayers;
    public transient int votes;
    public final DodgeballTeam TEAM_1, TEAM_2;
    protected final CubeSerializable area;
    protected LocationSerializable spawn;
    protected final String name;
    protected DodgeballManager mm;
    private int[] teamPlayerCount = new int[DodgeballTeam.values().length];
    private transient boolean isStarted = false;
    private final CubeSerializable TEAM_1_AREA;
    private final CubeSerializable TEAM_2_AREA;
    private final CubeSerializable SPECTATE_AREA;
    private final Random rand = new Random();
    private final double TEAM_1_SPAWN_X, TEAM_2_SPAWN_X;
    private transient boolean preStart = false;
    private boolean preStop = false;

    public Dodgeball(Plugin plug, Location minPoint, Location maxPoint, String name,
            DodgeballTeam team1, DodgeballTeam team2) {
        if(minPoint.equals(maxPoint)) {
            throw new IllegalArgumentException("Arena size is zero!");
        }
        this.area = new CubeSerializable(minPoint, maxPoint);
        this.name = name;
        this.mm = plug.getMinigameManager();
        mm.addMinigame(this);
        World w = minPoint.getWorld();
        double minX1 = minPoint.getX() + .5D;//Block center = block position + 0.5
        double minY1 = minPoint.getY() + .5D;
        double minZ1 = minPoint.getZ() + .5D;
        double maxX1 = (minX1 + maxPoint.getX() + .5D) / 2;
        double maxY1 = maxPoint.getY() + .5D;
        double maxZ1 = maxPoint.getZ() + .5D;
        TEAM_1_AREA = new CubeSerializable(new LocationSerializable(w, minX1, minY1, minZ1),
                new LocationSerializable(w, maxX1, maxY1, maxZ1));
        double minX2 = maxX1;
        double minY2 = minY1;
        double minZ2 = minZ1;
        double maxX2 = maxPoint.getX() + .5D;
        double maxY2 = maxY1;
        double maxZ2 = maxZ1;
        TEAM_2_AREA = new CubeSerializable(new LocationSerializable(w, minX2, minY2, minZ2),
                new LocationSerializable(w, maxX2, maxY2, maxZ2));
        this.TEAM_1 = team1;
        this.TEAM_2 = team2;
        TEAM_1_SPAWN_X = minX1 + 1 + 0.5D;
        TEAM_2_SPAWN_X = maxX2 - 1 + 0.5D;
        SPECTATE_AREA = new CubeSerializable(new LocationSerializable(minPoint).add(new Vector(0, 5,
                0)).getLocation(), maxPoint);
        this.maxPlayers = (int)Math.abs(((maxPoint.getX() - minPoint.getX()) * (maxPoint.getZ()
                - minPoint.getZ()) / (8F + 1F / 3F)));
    }

    public void reinit() {
        playerList = new ArrayList<DodgeballPlayer>(maxPlayers + 5);
        teamPlayerCount = new int[DodgeballTeam.values().length];
        isStarted = false;
        players = 0;
        votes = 0;
        preStart = false;
        preStop = false;
    }

    public void handlePlayerMove(PlayerMoveEvent e) {
        DodgeballPlayer p = mm.getMinigamePlayer(e.getPlayer().getName());
        p.update(mm, getPlayerTeamArea(p), SPECTATE_AREA, e.getTo());
    }

    public void handlePlayerInventoryClick(InventoryClickEvent e) {
        if(e.getSlotType() == ARMOR) {
            e.setCancelled(true);//Doesn't work in creative mode (Bukkit bug)
            ((Player)e.getWhoClicked()).updateInventory();
        }
    }

    public void handleEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if(!isStarted) {
            e.setCancelled(true);
            return;
        }
        Snowball s = (Snowball)e.getDamager();
        Player player = (Player)e.getEntity();
        Player shooter = (Player)s.getShooter();
        World w = s.getWorld();
        if(!mm.hasPlayer(player.getName()) || !mm.hasPlayer(shooter.getName())) {
            return;
        }
        if(player.getName().equals(shooter.getName())) {
            e.setCancelled(true);
            return;
        }
        DodgeballPlayer mp = mm.getMinigamePlayer(player.getName());
        DodgeballPlayer ms = mm.getMinigamePlayer(shooter.getName());
        if(mp.getTeam() == ms.getTeam()) {
            e.setCancelled(true);
            return;
        }
		//Location itemPos = player.getLocation();
        //w.dropItemNaturally(itemPos.add(0, 1, 0), new ItemStack(Material.SNOW_BALL, 1));//item is dropped onProjectileHitEvent
        s.remove();
        setPlayerAtRandomLocation(player);

        --mp.health;
        mp.update(mm, getPlayerTeamArea(mp), SPECTATE_AREA, player.getLocation());
    }

    public void handleProjectileHitEvent(ProjectileHitEvent e) {
        if(e.getEntity() instanceof Snowball) {
            Snowball s = (Snowball)e.getEntity();
            if(area.isInArea(s.getLocation())) {
                s.getWorld().
                        dropItemNaturally(s.getLocation(), new ItemStack(Material.SNOW_BALL, 1));
                s.remove();
            }
        }
    }

    public boolean hasTeam(String team) {
        return TEAM_1.toString().equals(team) || TEAM_2.toString().equals(team);
    }

    public void onStop() {
        preStop = false;
        area.removeNonPlayerEntities();
        reinit();
    }

    public void onStart() {
        preStop = false;
        preStart = false;
        isStarted = true;
        DodgeballPlayer parray[] = new DodgeballPlayer[1];
        parray = playerList.toArray(parray);
        Player pl = (parray[rand.nextInt(parray.length)]).getPlayer();
        pl.setItemInHand(new ItemStack(Material.SNOW_BALL, 3));
        for(DodgeballPlayer p: parray) {
            setPlayerAtRandomLocation(pl);
            p.getPlayer().sendMessage(ChatColor.GREEN + "Minigame Started!");
        }
    }

    public void onPlayrJoin(DodgeballPlayer p) {
        teamPlayerCount[p.getTeam().ordinal()]++;
        players++;
        playerList.add(p);
    }

    public void onPlayerLeave(DodgeballPlayer p) {
        players--;
        teamPlayerCount[p.getTeam().ordinal()]--;
        playerList.remove(p);
        stopIfDone();
    }

    public double getSpawnX(DodgeballPlayer p) {
        DodgeballTeam t = p.getTeam();
        return t == TEAM_1 ? TEAM_1_SPAWN_X : TEAM_2_SPAWN_X;
    }

    public void onPlayerDeath(DodgeballPlayer p) {
        teamPlayerCount[p.getTeam().ordinal()]--;
        p.isSpectator = true;
        stopIfDone();
    }

    public void preStart() {
        preStart = true;
    }

    public boolean isPerStart() {
        return preStart;
    }

    public void preStop() {
        preStop = true;
    }

    public boolean canStart() {
        return !(isStarted || preStart);
    }

    public boolean canJoin() {
        return !isStarted;
    }

    public boolean canStop() {
        return !(preStop || !isStarted);
    }

    public DodgeballTeam autoSelectTeam() {
        if(teamPlayerCount[TEAM_1.ordinal()] > teamPlayerCount[TEAM_2.ordinal()]) {
            return TEAM_2;
        }
        if(teamPlayerCount[TEAM_1.ordinal()] < teamPlayerCount[TEAM_2.ordinal()]) {
            return TEAM_1;
        }

        return rand.nextBoolean() ? DodgeballTeam.values()[TEAM_1.ordinal()]
                : DodgeballTeam.values()[TEAM_2.ordinal()];
    }

    public DodgeballTeam getWinnerTeam() {
        if(teamPlayerCount[TEAM_1.ordinal()] > teamPlayerCount[TEAM_2.ordinal()]) {
            return TEAM_1;
        }
        if(teamPlayerCount[TEAM_2.ordinal()] > teamPlayerCount[TEAM_1.ordinal()]) {
            return TEAM_2;
        }
        return null;
    }

    public Location getSpawn() {
        if(spawn == null) {
            return null;
        }
        return spawn.getLocation();
    }

    public void setSpawn(Location loc) {
        this.spawn = new LocationSerializable(loc);
    }

    public String getName() {
        return this.name;
    }

    public int getTeamPlayers(DodgeballTeam t) {
        return teamPlayerCount[t.ordinal()];
    }

    protected void setPlayerAtRandomLocation(Player player) {
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

    private CubeSerializable getPlayerTeamArea(DodgeballPlayer p) {
        if(p.getTeam() == TEAM_1) {
            return TEAM_1_AREA;
        } else {
            return TEAM_2_AREA;
        }
    }

    private boolean isDone() {
        return players > 0
                && (teamPlayerCount[TEAM_1.ordinal()] <= 0 || teamPlayerCount[TEAM_2.ordinal()] <= 0);
    }

    private void stopIfDone() {
        if(isDone() && !(preStop || preStart)) {
            mm.stopMinigame(this);
        }
    }
}

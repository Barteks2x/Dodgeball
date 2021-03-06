package com.github.barteks2x.dodgeball;

import java.util.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaCreator extends BukkitRunnable {

    private final Location minPoint, maxPoint;
    private final BlockData floor, wall1, wall2, line;
    private final int sectionHeight;
    private final List<BlockData> blockUpdates;
    private final World w;

    private static int sgn(int x) {
        return x < 0 ? -1 : 1;
    }

    public ArenaCreator(Location minPoint, Location maxPoint, BlockData floorBlock, BlockData wallBlock1, BlockData wallBlock2, BlockData lineBlock, int wallSectionHeight, Plugin plugin) {
        this.blockUpdates = new LinkedList<BlockData>();
        this.minPoint = minPoint;
        this.maxPoint = maxPoint;
        this.floor = floorBlock;
        this.wall1 = wallBlock1;
        this.wall2 = wallBlock2;
        this.line = lineBlock;
        this.sectionHeight = wallSectionHeight;
        this.w = minPoint.getWorld();
    }

    public void run() {
        World worldObj = minPoint.getWorld();
        int minX = minPoint.getBlockX();
        int minY = minPoint.getBlockY();
        int minZ = minPoint.getBlockZ();
        int maxX = maxPoint.getBlockX();
        int maxY = maxPoint.getBlockY();
        int maxZ = maxPoint.getBlockZ();
        for(int x = minX; x <= maxX; ++x) {
            for(int y = minY; y <= maxY; ++y) {
                for(int z = minZ; z <= maxZ; ++z) {
                    blockUpdates.add(new BlockData(0, 0, new Location(worldObj, x, y, z)));
                }
            }
        }
        for(int x = minX; x <= maxX; ++x) {
            for(int z = minZ; z <= maxZ; ++z) {
                blockUpdates.add(new BlockData(floor.id, floor.meta, new Location(worldObj, x, minY, z)));
            }
        }
        for(int x = minX; x <= maxX; ++x) {
            for(int y = minY; y <= maxY; ++y) {
                if(y < sectionHeight + minY) {
                    blockUpdates.add(new BlockData(wall1.id, wall2.meta, new Location(worldObj,
                            x, y, minZ)));
                    blockUpdates.add(new BlockData(wall1.id, wall2.meta, new Location(worldObj,
                            x, y, maxZ)));
                } else {
                    blockUpdates.add(
                            new BlockData(wall2.id, wall2.meta, new Location(worldObj, x, y, minZ)));
                    blockUpdates.add(
                            new BlockData(wall2.id, wall2.meta, new Location(worldObj, x, y, maxZ)));
                }

            }
        }
        for(int z = minZ; z <= maxZ; ++z) {
            for(int y = minY; y <= maxY; ++y) {
                if(y < sectionHeight + minY) {
                    blockUpdates.add(
                            new BlockData(wall1.id, wall1.meta, new Location(worldObj, minX, y, z)));
                    blockUpdates.add(
                            new BlockData(wall1.id, wall1.meta, new Location(worldObj, maxX, y, z)));
                } else {
                    blockUpdates.add(
                            new BlockData(wall2.id, wall2.meta, new Location(worldObj, minX, y, z)));
                    blockUpdates.add(
                            new BlockData(wall2.id, wall2.meta, new Location(worldObj, maxX, y, z)));
                }
            }
        }
        if((minX + maxX) % 2 == 0) {
            int lineX = (minX + maxX) / 2;
            for(int z = minZ; z <= maxZ; ++z) {
                blockUpdates.add(new BlockData(line.id, line.meta,
                        new Location(worldObj, lineX, minY, z)));
            }
        } else {
            int lineX1 = (minX + maxX) / 2;
            int lineX2 = (minX + maxX) / 2 + sgn(lineX1);//I have no idea why it must be Math.signum, if it is 1 or -1 line is shifted by one on negative/positive X positions
            for(int z = minZ; z <= maxZ; ++z) {
                blockUpdates.add(new BlockData(line.id, line.meta,
                        new Location(worldObj, lineX1, minY, z)));
                blockUpdates.add(new BlockData(line.id, line.meta,
                        new Location(worldObj, lineX2, minY, z)));
            }
        }
        runTasks();
    }

    private void runTasks() {
        final Iterator<BlockData> it = blockUpdates.iterator();
        final BukkitRunnable t = this;
        while(it.hasNext()) {
            new BukkitRunnable() {
                public void run() {
                    synchronized(t) {
                        for(int i = 0; i < 4096; ++i) {
                            if(it.hasNext()) {
                                BlockData b = it.next();
                                Location l = b.loc;
                                w.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ()).
                                        setTypeIdAndData(b.id, b.meta, false);
                            }
                        }

                        t.notify();
                    }

                }
            }.runTaskLater(Plugin.plug, 1);
            synchronized(t) {
                try {
                    t.wait();
                } catch(InterruptedException ex) {
                }
            }
        }
    }
}

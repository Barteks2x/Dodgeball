package com.github.barteks2x.minielementgaming;

import java.util.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaCreator extends BukkitRunnable {

	private Location minPoint, maxPoint;
	private BlockData floor, wall1, wall2, line;
	private int sectionHeight;
	private List<BlockData> blockUpdates;
	private final Plugin plugin;
	private final World w;

	public ArenaCreator(Location minPoint, Location maxPoint, BlockData floorBlock,
			BlockData wallBlock1, BlockData wallBlock2, BlockData lineBlock,
			int wallSectionHeight, Plugin plugin) {
		this.blockUpdates = new LinkedList<BlockData>();
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
		this.floor = floorBlock;
		this.wall1 = wallBlock1;
		this.wall2 = wallBlock2;
		this.line = lineBlock;
		this.sectionHeight = wallSectionHeight;
		this.plugin = plugin;
		this.w = minPoint.getWorld();
	}

	public void run() {
		World w = minPoint.getWorld();
		int minX = minPoint.getBlockX();
		int minY = minPoint.getBlockY();
		int minZ = minPoint.getBlockZ();
		int maxX = maxPoint.getBlockX();
		int maxY = maxPoint.getBlockY();
		int maxZ = maxPoint.getBlockZ();
		for (int x = minX; x <= maxX; ++x) {
			for (int y = minY; y <= maxY; ++y) {
				for (int z = minZ; z <= maxZ; ++z) {
					blockUpdates.add(new BlockData(0, 0, new LocationSerializable(w, x, y, z)));
				}
			}
		}
		for (int x = minX; x <= maxX; ++x) {
			for (int z = minZ; z <= maxZ; ++z) {
				blockUpdates.add(new BlockData(floor.id, floor.meta, new LocationSerializable(w, x,
						minY, z)));
			}
		}
		for (int x = minX; x <= maxX; ++x) {
			for (int y = minY; y <= maxY; ++y) {
				if (y < sectionHeight + minY) {
					blockUpdates.add(new BlockData(wall1.id, wall2.meta, new LocationSerializable(w,
							x, y, minZ)));
					blockUpdates.add(new BlockData(wall1.id, wall2.meta, new LocationSerializable(w,
							x, y, maxZ)));
				} else {
					blockUpdates.add(new BlockData(wall2.id, wall2.meta, new LocationSerializable(w,
							x, y, minZ)));
					blockUpdates.add(new BlockData(wall2.id, wall2.meta, new LocationSerializable(w,
							x, y, maxZ)));
				}

			}
		}
		for (int z = minZ; z <= maxZ; ++z) {
			for (int y = minY; y <= maxY; ++y) {
				if (y < sectionHeight + minY) {
					blockUpdates.add(new BlockData(wall1.id, wall1.meta, new LocationSerializable(w,
							minX, y, z)));
					blockUpdates.add(new BlockData(wall1.id, wall1.meta, new LocationSerializable(w,
							maxX, y, z)));
				} else {
					blockUpdates.add(new BlockData(wall2.id, wall2.meta, new LocationSerializable(w,
							minX, y, z)));
					blockUpdates.add(new BlockData(wall2.id, wall2.meta, new LocationSerializable(w,
							maxX, y, z)));
				}
			}
		}
		if ((minX + maxX) % 2 == 0) {
			int lineX = (minX + maxX) / 2;
			for (int z = minZ; z <= maxZ; ++z) {
				blockUpdates.add(new BlockData(line.id, line.meta,
						new LocationSerializable(w, lineX, minY, z)));
			}
		} else {
			int lineX1 = (minX + maxX) / 2;
			int lineX2 = (minX + maxX) / 2 + sgn(lineX1);//I have no idea why it must be Math.signum, if it is 1 or -1 line is shifted by one on negative/positive X positions
			for (int z = minZ; z <= maxZ; ++z) {
				blockUpdates.add(new BlockData(line.id, line.meta,
						new LocationSerializable(w, lineX1, minY, z)));
				blockUpdates.add(new BlockData(line.id, line.meta,
						new LocationSerializable(w, lineX2, minY, z)));
			}
		}
		runTasks();
	}

	private static int sgn(int x) {
		return x < 0 ? -1 : 1;
	}

	private void runTasks() {
		final Iterator<BlockData> it = blockUpdates.iterator();
		final BukkitRunnable t = this;
		while (it.hasNext()) {
			new BukkitRunnable() {
				public void run() {
					synchronized (t) {
						for (int i = 0; i < 4096; ++i) {
							if (it.hasNext()) {
								BlockData b = it.next();
								Location l = b.loc.getLocation();
								w.getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ()).
										setTypeIdAndData(b.id, b.meta, false);
							}
						}

						t.notify();//Exception...
					}

				}
			}.runTaskLater(plugin, 1);
			synchronized (t) {
				try {
					t.wait();
				} catch (InterruptedException ex) {
				}
			}
		}
	}
}

package org.barteks2x.minielementgaming;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaCreator extends BukkitRunnable {

	Location minPoint, maxPoint;
	BlockData floor, wall1, wall2, line;
	int sectionHeight;

	public ArenaCreator(Location minPoint, Location maxPoint, BlockData floorBlock,
			BlockData wallBlock1, BlockData wallBlock2, BlockData lineBlock,
			int wallSectionHeight) {
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
		this.floor = floorBlock;
		this.wall1 = wallBlock1;
		this.wall2 = wallBlock2;
		this.line = lineBlock;
		this.sectionHeight = wallSectionHeight;
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
					w.getBlockAt(x, y, z).setTypeIdAndData(0, (byte)0, false);
				}
			}
		}
		for (int x = minX; x <= maxX; ++x) {
			for (int z = minZ; z <= maxZ; ++z) {
				w.getBlockAt(x, minY, z).setTypeIdAndData(floor.id, floor.meta, false);
			}
		}
		for (int x = minX; x <= maxX; ++x) {
			for (int y = minY; y <= maxY; ++y) {
				if (y < sectionHeight + minY) {
					w.getBlockAt(x, y, minZ).setTypeIdAndData(wall1.id, wall1.meta, false);
					w.getBlockAt(x, y, maxZ).setTypeIdAndData(wall1.id, wall1.meta, false);
				} else {
					w.getBlockAt(x, y, minZ).setTypeIdAndData(wall2.id, wall2.meta, false);
					w.getBlockAt(x, y, maxZ).setTypeIdAndData(wall2.id, wall2.meta, false);
				}

			}
		}
		for (int z = minZ; z <= maxZ; ++z) {
			for (int y = minY; y <= maxY; ++y) {
				if (y < sectionHeight + minY) {
					w.getBlockAt(minX, y, z).setTypeIdAndData(wall1.id, wall1.meta, false);
					w.getBlockAt(maxX, y, z).setTypeIdAndData(wall1.id, wall1.meta, false);
				} else {
					w.getBlockAt(minX, y, z).setTypeIdAndData(wall2.id, wall2.meta, false);
					w.getBlockAt(maxX, y, z).setTypeIdAndData(wall2.id, wall2.meta, false);
				}
			}
		}
		if ((minX + maxX) % 2 == 0) {
			int lineX = (minX + maxX) / 2;
			for (int z = minZ; z <= maxZ; ++z) {
				w.getBlockAt(lineX, minY, z).setTypeIdAndData(line.id, line.meta, false);
			}
		} else {
			int lineX1 = (minX + maxX) / 2;
			int lineX2 = (minX + maxX) / 2 + sgn(lineX1);//I have no idea why it must be Math.signum, if I change it to 1 (-1) line is shifted by one on negative/positive positions
			for (int z = minZ; z <= maxZ; ++z) {
				w.getBlockAt(lineX1, minY, z).setTypeIdAndData(line.id, line.meta, false);
				w.getBlockAt(lineX2, minY, z).setTypeIdAndData(line.id, line.meta, false);
			}
		}
	}

	private static int sgn(int x) {
		return x < 0 ? -1 : 1;
	}
}

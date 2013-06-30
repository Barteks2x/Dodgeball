package org.barteks2x.minielementgaming;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Plugin extends JavaPlugin {

	int minArenaSizeXZ = 10;
	int minArenaSizeY = 4;
	public WorldEditPlugin worldedit = null;
	private String noargsmsg =
			"Usage: /makearena <floor-block-id>:<floor-block-metadata> <wall-block-id>:<wall-block-metadata> <roof-block-id>:<roof-block-metadata> [line-block-id]:[line-block-metadata]";
	private String noselectionmsg = "Nothing selected. You must select arena!";
	private String toosmallarenamsg = "Too small arena! Arena must be at least " + minArenaSizeXZ +
			"x" +
			minArenaSizeY + "x" + minArenaSizeXZ;

	@Override
	public void onEnable() {
		worldedit = (WorldEditPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");

	}

	@Override
	public void onDisable() {
	}

	public static void main(String[] args) {
		//Main method, inform that this is bukkit plugin
		System.out.println("This is bukkit plugin! Place it in plugins folder and run Bukkit.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if ("makearena".equalsIgnoreCase(cmd.getName())) {
			makearena(sender, cmd, label, args);
			return true;
		}
		if ("join".equalsIgnoreCase(cmd.getName())) {
			join(sender, cmd, label, args);
		}
		return false;
	}

	private void makearena(CommandSender sender, Command cmd, String label, String[] args) {
		Selection sel = worldedit.getSelection((Player)sender);
		if (sel == null) {
			sender.sendMessage(noselectionmsg);
			return;
		}
		if (sel.getHeight() < minArenaSizeY || sel.getWidth() < minArenaSizeXZ || sel.
				getLength() < minArenaSizeXZ) {
			sender.sendMessage(toosmallarenamsg);
			return;
		}
		Location maxPoint = sel.getMaximumPoint();
		Location minPoint = sel.getMinimumPoint();
		if (args.length < 5) {
			sender.sendMessage(noargsmsg);
			return;
		}
		BlockData[] blocks = new BlockData[4];
		for (int i = 0; i < blocks.length; ++i) {
			String arg = args[i];
			if (arg.contains(":")) {
				String[] blockDataString = arg.split(":");
				if (blockDataString.length != 2) {
					sender.sendMessage("Wrong parameter: " + arg);
					return;
				}
				try {
					blocks[i] = new BlockData(Integer.valueOf(blockDataString[0]), Integer.
							valueOf(blockDataString[1]));
				} catch (Exception ex) {
					sender.sendMessage("Wrong parameter: " + arg);
					return;
				}
			} else {
				try {
					blocks[i] = new BlockData(Integer.valueOf(arg));
				} catch (Exception ex) {
					sender.sendMessage("Wrong parameter: " + arg);
					return;
				}
			}
		}
		int sectionHeight;
		try {
			sectionHeight = Integer.valueOf(args[3]);
		} catch (Exception ex) {
			sender.sendMessage("Wrong parameter: " + args[3]);
			return;
		}
		if (blocks[3] == null) {
			blocks[3] = blocks[0];
		}
		ArenaCreator creator = new ArenaCreator(minPoint, maxPoint, blocks[0], blocks[1],
				blocks[2], blocks[3], sectionHeight);
		creator.runTaskLater(this, 1);
		return;
	}

	private void join(CommandSender sender, Command cmd, String label, String[] args) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	private class ArenaCreator extends BukkitRunnable {

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
		}
	}
}

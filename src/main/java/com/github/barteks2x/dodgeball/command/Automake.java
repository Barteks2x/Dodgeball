package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.*;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class Automake {

	private final MinigameManager mm;
	private final WorldEditPlugin worldedit;
	private final Plugin plugin;
	private final int minArenaSizeXZ = 10;
	private final int minArenaSizeY = 4;
	private final String noselectionmsg = "Nothing selected. You must select arena!";
	private final String toosmallarenamsg = "Too small arena! Arena must be at least " +
			minArenaSizeXZ + "x" + minArenaSizeY + "x" + minArenaSizeXZ;

	public Automake(MinigameManager mm, Plugin plugin, WorldEditPlugin worldedit) {
		this.mm = mm;
		this.plugin = plugin;
		this.worldedit = worldedit;
	}

	@DBCommand
	public boolean automake(CommandSender sender, Iterator<String> args) {
		if (!sender.hasPermission("db.automake")) {
			sender.sendMessage(RED + "You don't have permission to use this command!");
			return true;
		}
		if (!args.hasNext()) {
			sender.sendMessage(
					"Not enough parameters!\n/minigame help <command1> <command2> ... for more info");
			return true;
		}
		String teamsString = args.next();
		String teams[] = teamsString.split(":");
		if (teams.length != 2) {
			sender.sendMessage(RED + "Wrong paraneter: " + teamsString);
			return true;
		}
		DodgeballTeam team1, team2;
		try {
			team1 = DodgeballTeam.valueOf(teams[0].toUpperCase());
			team2 = DodgeballTeam.valueOf(teams[1].toUpperCase());
		} catch (IllegalArgumentException ex) {
			sender.sendMessage(YELLOW + "Incorrect color");
			return true;
		}

		Selection sel = worldedit.getSelection((Player)sender);
		if (sel == null) {
			sender.sendMessage(noselectionmsg);
			return true;
		}
		if (sel.getHeight() < minArenaSizeY || sel.getWidth() < minArenaSizeXZ || sel.
				getLength() < minArenaSizeXZ) {
			sender.sendMessage(toosmallarenamsg);
			return true;
		}
		Location maxPoint = sel.getMaximumPoint();
		Location minPoint = sel.getMinimumPoint();
		if (!args.hasNext()) {
			sender.sendMessage(
					"Not enough parameters!\n/minigame help <command1> <command2> ... for more info");
			return true;
		}
		String name = args.next();
		BlockData[] blocks = new BlockData[4];
		for (int i = 0; i < blocks.length; ++i) {
			if (!args.hasNext()) {
				sender.sendMessage(
						"Not enough parameters!\n/minigame help <command1> <command2> ... for more info");
				return true;
			}
			String par = args.next();
			try {
				blocks[i] = new BlockData(par);
			} catch (Exception ex) {
				sender.sendMessage(RED + "Wrong paraneter: " + par);
				return true;
			}
		}
		int sectionHeight;
		if (!args.hasNext()) {
			sender.sendMessage(
					"Not enough parameters!\n/minigame help <command1> <command2> ... for more info");
			return true;
		}
		String par = args.next();
		try {
			sectionHeight = Integer.valueOf(par);
		} catch (Exception ex) {
			sender.sendMessage(RED + "Wrong parameter: " + par);
			return true;
		}
		ArenaCreator creator = new ArenaCreator(minPoint, maxPoint, blocks[0],
				blocks[1], blocks[2], blocks[3], sectionHeight, plugin);
		creator.runTaskAsynchronously(plugin);//avoid lag when command is executed
		Minigame arena = new Dodgeball(plugin, minPoint, maxPoint, name, team1, team2);
		mm.addMinigame(arena);
		sender.sendMessage(GREEN + "Building dodgeball arena...");
		return true;
	}

	@DBCommand
	public boolean am(CommandSender sender, Iterator<String> args) {
		return this.automake(sender, args);
	}
}

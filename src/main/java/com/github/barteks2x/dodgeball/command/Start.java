package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.Dodgeball;
import com.github.barteks2x.dodgeball.DodgeballManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.RED;

public class Start {

	private final DodgeballManager mm;

	public Start(DodgeballManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean start(CommandSender sender, Iterator<String> args) {
		if (!sender.hasPermission("db.start")) {
			sender.sendMessage(RED + "You don't have permission to use this command!");
			return true;
		}

		if (!args.hasNext()) {
			sender.sendMessage(
					"Not enough parameters!\n/minigame help <command1> <command2> ... for more info");
			return false;
		}
		String name = args.next();
		final Dodgeball m = mm.getMinigame(name);
		if (m == null) {
			sender.sendMessage(RED + "Minigame doesn't exist!");
			return true;
		}
		mm.startMinigameDelayed(m);
		return true;
	}
}

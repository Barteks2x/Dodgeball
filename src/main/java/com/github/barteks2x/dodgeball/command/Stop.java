package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.DodgeballManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.RED;

public class Stop {

	private final DodgeballManager mm;

	public Stop(DodgeballManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean stop(CommandSender sender, Iterator<String> args) {
		if (!sender.hasPermission("db.stop")) {
			sender.sendMessage(RED + "You don't have permission to use this command!");
			return true;
		}
		if (!args.hasNext()) {
			sender.sendMessage("Not enough parameters!");
			return true;
		}
		mm.stopMinigame(args.next());
		return true;
	}
}

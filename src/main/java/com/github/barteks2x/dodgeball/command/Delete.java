package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.DodgeballManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.RED;

public class Delete {

	private final DodgeballManager mm;

	public Delete(DodgeballManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean delete(CommandSender sender, Iterator<String> args) {
		if (!sender.hasPermission("db.delete")) {
			sender.sendMessage(RED + "You don't have permission to use this command!");
			return true;
		}
		if (!args.hasNext()) {
			sender.sendMessage("Not enough parameters!");
			return true;
		}
		mm.removeMinigame(mm.getMinigame(args.next()));
		return true;
	}
}

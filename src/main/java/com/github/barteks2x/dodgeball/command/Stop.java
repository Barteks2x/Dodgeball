package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.*;
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
		Dodgeball db = mm.getMinigame(args.next());
		db.isStarted = false;
		DodgeballPlayer mpl[] = new DodgeballPlayer[10];
		mpl = db.playerList.toArray(mpl);
		for (DodgeballPlayer dbp : mpl) {
			mm.removePlayer(dbp);
			dbp.getPlayer().sendMessage("Minigame stopped.");
		}
		return true;
	}
}

package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.Dodgeball;
import com.github.barteks2x.dodgeball.DodgeballManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.RED;

public class Vote {

	private final DodgeballManager mm;

	public Vote(DodgeballManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean vote(CommandSender sender, Iterator<String> args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can be used only by player");
			return true;
		}
		if (!sender.hasPermission("db.vote")) {
			sender.sendMessage(RED + "You don't have permission to use this command!");
			return true;
		}

		Dodgeball m = mm.getPlayerMinigame(sender.getName());
		mm.vote(m);
		return true;
	}
}

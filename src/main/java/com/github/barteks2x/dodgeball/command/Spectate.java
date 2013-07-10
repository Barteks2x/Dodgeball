package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.*;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class Spectate {

	private final DodgeballManager mm;

	public Spectate(DodgeballManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean spawn(CommandSender sender, Iterator<String> args) {
		if (!sender.hasPermission("db.spectate")) {
			sender.sendMessage(RED + "You don't have permission to use this command!");
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command must be executed by player");
			return true;
		}
		if (!args.hasNext()) {
			sender.sendMessage(YELLOW + "No minigame specified!");
			return true;
		}
		String n = args.next();
		String p = ((Player)sender).getName();
		if (mm.hasPlayer(p)) {
			sender.sendMessage(YELLOW +
					"You can't spectate two minigames! Use /leave to leave current minigame");
			return true;
		}
		Dodgeball m = mm.getMinigame(n);
		if (m == null) {
			sender.sendMessage(RED +
					"Minigame not exist! Are you sure name is correct? \"Mini\" and \"MiNi\" are different minigames!");
			return true;
		}
		DodgeballPlayer mp = mm.createPlayer((Player)sender, m);
		mm.addPlayer(mp);
		mm.setPlayerSpactate(mp);
		sender.sendMessage(GREEN + "Joined to minigame: " + n);
		return true;
	}
}

package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.Dodgeball;
import com.github.barteks2x.dodgeball.DodgeballManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class Join {

	private final DodgeballManager mm;

	public Join(DodgeballManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean join(CommandSender sender, Iterator<String> args) {
		if (!sender.hasPermission("db.join")) {
			sender.sendMessage(RED + "You don't have permission to use this command!");
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command must be executed by player");
			return true;
		}
		if (!args.hasNext()) {
			sender.sendMessage(RED + "No minigame specified!");
			return true;
		}
		String n = args.next();
		String p = ((Player)sender).getName();
		Dodgeball m = mm.getMinigame(n);
		if (m == null) {
			sender.sendMessage(RED +
					"Minigame not exist! Are you sure name is correct?\"Mini\" and \"MiNi\" are different minigames!");
			return true;
		}
		if (!m.canJoin()) {
			sender.sendMessage(YELLOW + "Minigame already started!");
			return true;
		}
		if (mm.hasPlayer(p)) {
			sender.sendMessage(YELLOW +
					"You can't join two minigames! Use /leave to leave current minigame");
			return true;

		}
		String team = sender.hasPermission("db.join.selectteam") ? args.hasNext() ? args.next().
				toUpperCase() : null : null;
		if (m.maxPlayers <= m.players && !sender.hasPermission("db.joinfull")) {
			sender.sendMessage(DARK_RED + "Arena full! You can't join!");
			return true;
		}
		mm.addPlayer(mm.createPlayer((Player)sender, m, team));
		sender.sendMessage(AQUA + "Joined to minigame: " + n);
		return true;
	}
}

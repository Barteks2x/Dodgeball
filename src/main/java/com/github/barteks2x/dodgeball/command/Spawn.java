package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.MinigameManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;

public class Spawn {

	private final MinigameManager mm;

	public Spawn(MinigameManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean spawn(CommandSender sender, Iterator<String> args) {
		if (!sender.hasPermission("db.setspawn")) {
			sender.sendMessage(RED + "You don't have permission to use this command!");
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can be used only by player");
			return true;
		}
		if (!args.hasNext()) {
			sender.sendMessage(YELLOW + "No minigame name specified!");
			return true;
		}
		String name = args.next();
		mm.getMinigame(name).setSpawn(((Player)sender).getLocation());
		sender.sendMessage(AQUA + "Dodgeball spawn set: " + ((Player)sender).getLocation().
				toString());
		return true;
	}

	@DBCommand
	public boolean setSpawn(CommandSender sender, Iterator<String> args) {
		return spawn(sender, args);
	}
}

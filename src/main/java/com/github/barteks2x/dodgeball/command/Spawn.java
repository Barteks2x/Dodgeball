package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.DodgeballManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.AQUA;
import static org.bukkit.ChatColor.RED;

public class Spawn {

	private final DodgeballManager mm;

	public Spawn(DodgeballManager mm) {
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
			mm.setGlobalSpawn(((Player)sender).getLocation());
			sender.sendMessage(AQUA + "Global dodgeball spawn set: " + ((Player)sender).
					getLocation().toString());
			return true;
		}
		String name = args.next();
		mm.getMinigame(name).setSpawn(((Player)sender).getLocation());
		sender.sendMessage(AQUA + name + " spawn set: " + ((Player)sender).getLocation().
				toString());
		return true;
	}

	@DBCommand
	@Deprecated
	public boolean setspawn(CommandSender sender, Iterator<String> args) {
		return spawn(sender, args);
	}
}

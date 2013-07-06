package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.Minigame;
import com.github.barteks2x.dodgeball.MinigameManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.ChatColor.RED;

public class Start {

	private final MinigameManager mm;

	public Start(MinigameManager mm) {
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
		final Minigame m = mm.getMinigame(name);
		if (m == null) {
			sender.sendMessage(RED + "Minigame doesn't exist!");
			return true;
		}
		new BukkitRunnable() {
			public void run() {
				m.onStart();
			}
		}.runTaskLater(null, 20 * 30);

		return true;
	}
}

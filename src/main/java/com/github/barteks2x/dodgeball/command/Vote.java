package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.Minigame;
import com.github.barteks2x.dodgeball.MinigameManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Vote {

	private final MinigameManager mm;

	public Vote(MinigameManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean vote(CommandSender sender, Iterator<String> args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can be used only by player");
			return true;
		}
		Minigame m = mm.getPlayerMinigame(sender.getName());
		mm.vote(m);
		return true;
	}
}

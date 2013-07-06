package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.MinigameManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;

public class Stop {

	private final MinigameManager mm;

	public Stop(MinigameManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean stop(CommandSender sender, Iterator<String> args) {
		return true;
	}
}

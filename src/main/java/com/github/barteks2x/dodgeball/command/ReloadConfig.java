package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.MinigameManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;

public class ReloadConfig {

	private final MinigameManager mm;

	public ReloadConfig(MinigameManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean reloadConfig(CommandSender sender, Iterator<String> args) {
		//TODO reloadconfig
		return true;
	}
}

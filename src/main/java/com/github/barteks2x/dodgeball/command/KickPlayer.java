package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.MinigameManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;

public class KickPlayer {

	private final MinigameManager mm;

	public KickPlayer(MinigameManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean kickPlayer(CommandSender sender, Iterator<String> args) {
		//TODO kickplayer
		return true;
	}
}

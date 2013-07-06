package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.MinigameManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;

public class Delete {

	private final MinigameManager mm;

	public Delete(MinigameManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean delete(CommandSender sender, Iterator<String> args) {
		//TODO delete
		return true;
	}
}

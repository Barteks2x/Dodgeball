package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.DodgeballManager;
import com.github.barteks2x.dodgeball.DodgeballPlayer;
import java.util.Iterator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.RED;

public class KickPlayer {

	private final DodgeballManager mm;

	public KickPlayer(DodgeballManager mm) {
		this.mm = mm;
	}

	@DBCommand
	public boolean kickPlayer(CommandSender sender, Iterator<String> args) {
		if (!sender.hasPermission("db.kickplayer")) {
			sender.sendMessage(RED + "You don't have permission to use this command!");
			return true;
		}
		if (!args.hasNext()) {
			sender.sendMessage("Not enough parameters!");
			return true;
		}
		String pname = args.next();
		DodgeballPlayer dp = mm.getMinigamePlayer(pname);
		mm.removePlayer(dp);
		dp.getPlayer().sendMessage("You are kicked from minigame by " + ((Player)sender).getName());
		return true;
	}
}

package org.barteks2x.minielementgaming;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MinigameListener implements Listener {

	private final Plugin plugin;

	public MinigameListener(Plugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		//TODO onPlayerMove
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		//TODO onPlayerInteract
	}

	public boolean addArena(ArenaBase arena) {
		//TODO addArena
		return false;
	}
}

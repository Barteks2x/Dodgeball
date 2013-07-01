package org.barteks2x.minielementgaming;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class DodgeBallArena extends ArenaBase {

	private static final long serialVersionUID = 3612989452554623L;

	public DodgeBallArena(Location minPoint, Location maxPoint) {
		super(minPoint, maxPoint, Minigame.DB, (byte)2);
	}

	@Override
	public void handlePlayerMove(PlayerMoveEvent e) {
		//TODO Auto-generated method
	}

	@Override
	public void handlePlayerInteract(PlayerInteractEvent e) {
		//TODO Auto-generated method
	}
}

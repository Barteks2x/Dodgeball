package org.barteks2x.minielementgaming;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class DodgeBallArena extends ArenaBase {

	private static final long serialVersionUID = 3612989452554623L;
	private final CubeSerializable team1area;
	private final CubeSerializable team2area;

	public DodgeBallArena(Location minPoint, Location maxPoint) {
		super(minPoint, maxPoint, Minigame.DB, (byte)2);
		World w = minPoint.getWorld();
		double minX1 = minPoint.getX();
		double minY1 = minPoint.getY();
		double minZ1 = minPoint.getZ();
		double maxX1 = (minPoint.getX() + maxPoint.getX()) / 2;
		double maxY1 = maxPoint.getY();
		double maxZ1 = maxPoint.getZ();
		team1area = new CubeSerializable(new LocationSerializable(w, minX1, minY1, minZ1),
				new LocationSerializable(w, maxX1, maxY1, maxZ1));
		double minX2 = maxX1;
		double minY2 = minY1;
		double minZ2 = minZ1;
		double maxX2 = maxPoint.getX();
		double maxY2 = maxY1;
		double maxZ2 = maxZ1;
		team2area = new CubeSerializable(new LocationSerializable(w, minX2, minY2, minZ2),
				new LocationSerializable(w, maxX2, maxY2, maxZ2));
	}

	@Override
	public void handlePlayerMove(PlayerMoveEvent e) {
		if (!isPlayerInArena(e.getPlayer())) {
			//TODO move player to arena if is outside
		}
		MinigamePlayer p = players.get(e.getPlayer().getName());
		CubeSerializable area;
		if (p.getTeam() == MinigameTeam.TEAM_1) {
			area = team1area;
		} else {
			area = team2area;
		}
		area.setPlayerInArea(e.getPlayer(), e.getTo());
	}

	@Override
	public void handlePlayerInteract(PlayerInteractEvent e) {
		//TODO Dodgeball player interact
	}
}

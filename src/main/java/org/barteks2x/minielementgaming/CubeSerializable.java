package org.barteks2x.minielementgaming;

import java.io.Serializable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CubeSerializable implements Serializable {

	private static final long serialVersionUID = 452948671293543267L;
	public final LocationSerializable minPoint;
	public final LocationSerializable maxPoint;

	public CubeSerializable(Location minPoint, Location maxPoint) {
		this(new LocationSerializable(minPoint), new LocationSerializable(maxPoint));
	}

	public CubeSerializable(LocationSerializable minPoint, LocationSerializable maxPoint) {
		this.maxPoint = maxPoint;
		this.minPoint = minPoint;
	}

	public boolean isPlayerInArea(Player p) {
		Location l = p.getLocation();
		double x = l.getX();
		double y = l.getY();
		double z = l.getZ();
		if (x > minPoint.x && x < maxPoint.x && z > minPoint.z && z < maxPoint.z && y > minPoint.y &&
				y < maxPoint.y) {
			return true;
		}
		return false;
	}

	public void setPlayerInArea(Player p, Location newPos) {
		int x = newPos.getBlockX();
		int y = newPos.getBlockY();
		int z = newPos.getBlockZ();
		boolean f = false;
		Location min = minPoint.getLocation();
		Location max = maxPoint.getLocation();
		if (x < min.getBlockX()) {
			newPos.setX(newPos.getX() + 1);
			f = true;
		}
		if (x > max.getBlockX()) {
			newPos.setX(newPos.getX() - 1);
			f = true;
		}
		if (y < min.getBlockX()) {
			newPos.setY(newPos.getY() + 1);
			f = true;
		}
		if (y > max.getBlockY()) {
			newPos.setY(newPos.getY() - 1);
			f = true;
		}
		if (z < min.getBlockZ()) {
			newPos.setZ(newPos.getZ() + 1);
			f = true;
		}
		if (z > max.getBlockZ()) {
			newPos.setZ(newPos.getZ() - 1);
			f = true;
		}
		if (f) {
			p.teleport(newPos);
			Vector v = p.getVelocity();
			p.setVelocity(new Vector(-v.getX(), -1, -v.getZ()));
		}
	}
}

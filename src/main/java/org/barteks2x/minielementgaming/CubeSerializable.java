package org.barteks2x.minielementgaming;

import java.io.Serializable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CubeSerializable implements Serializable {

	private static final long serialVersionUID = 452948671293543267L;
	public final LocationSerializable minPoint;
	public final LocationSerializable maxPoint;

	public CubeSerializable(Location minPoint, Location maxPoint) {
		this(new LocationSerializable(minPoint), new LocationSerializable(maxPoint));
	}

	public CubeSerializable(LocationSerializable minPoint, LocationSerializable maxPoint) {
		//double x1 = minPoint.x;
		//double y1 = minPoint.y;
		//double z1 = minPoint.z;
		//double x2 = maxPoint.x;
		//double y2 = maxPoint.y;
		//double z2 = maxPoint.z;
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
		double x = newPos.getX();
		double y = newPos.getY();
		double z = newPos.getZ();
		boolean f = false;
		Location min = minPoint.getLocation();
		Location max = maxPoint.getLocation();
		if (x < min.getX()) {
			newPos.setX(newPos.getX() + 1);
			f = true;
		}
		if (x > max.getX()) {
			newPos.setX(newPos.getX() - 1);
			f = true;
		}
		if (y < min.getY()) {
			newPos.setY(newPos.getY() + 1);
			f = true;
		}
		if (y > max.getY()) {
			newPos.setY(newPos.getY() - 1);
			f = true;
		}
		if (z < min.getZ()) {
			newPos.setZ(newPos.getZ() + 1);
			f = true;
		}
		if (z > max.getZ()) {
			newPos.setZ(newPos.getZ() - 1);
			f = true;
		}
		if (f) {
			p.teleport(newPos);
		}
	}
}

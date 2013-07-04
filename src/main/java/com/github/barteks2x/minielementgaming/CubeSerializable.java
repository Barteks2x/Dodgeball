package com.github.barteks2x.minielementgaming;

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
		this.maxPoint = maxPoint;
		this.minPoint = minPoint;
	}

	public boolean isInArea(Location l) {
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
			newPos.setX(min.getX() + 1);
			f = true;
		}
		if (x > max.getX()) {
			newPos.setX(max.getX() - 1);
			f = true;
		}
		if (y < min.getY()) {
			newPos.setY(min.getY() + 1);
			f = true;
		}
		if (y > max.getY()) {
			newPos.setY(max.getY() - 1);
			f = true;
		}
		if (z < min.getZ()) {
			newPos.setZ(min.getZ() + 1);
			f = true;
		}
		if (z > max.getZ()) {
			newPos.setZ(max.getZ() - 1);
			f = true;
		}
		if (newPos.getWorld() != min.getWorld()) {
			newPos.setWorld(min.getWorld());
			f = true;
		}
		if (f) {
			p.teleport(newPos);
		}
	}
}

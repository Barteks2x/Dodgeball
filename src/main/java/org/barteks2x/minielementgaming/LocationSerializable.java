package org.barteks2x.minielementgaming;

import java.io.Serializable;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializable extends Location implements Serializable {

	private static final long serialVersionUID = 1L;

	public LocationSerializable(World world, double x, double y, double z) {
		super(world, x, y, z);
	}

	public LocationSerializable(World world, double x, double y, double z, float yaw, float pitch) {
		super(world, x, y, z, yaw, pitch);
	}

	public LocationSerializable(Location l) {
		this(l.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
	}
}

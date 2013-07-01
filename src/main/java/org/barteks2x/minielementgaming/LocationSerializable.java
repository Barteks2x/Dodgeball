package org.barteks2x.minielementgaming;

import java.io.Serializable;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationSerializable implements Serializable {

	private static final long serialVersionUID = 1L;
	public final double x, y, z, yaw, pitch;
	public String world;
	private transient World worldObj;

	public LocationSerializable(World world, double x, double y, double z) {
		this(world, x, y, z, 0, 0);
	}

	public LocationSerializable(World world, double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.world = world.getName().trim();
		this.worldObj = world;
	}

	public LocationSerializable(Location l) {
		this(l.getWorld(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
	}

	public Location getLocation() {
		return new Location(worldObj, x, y, z);
	}
}

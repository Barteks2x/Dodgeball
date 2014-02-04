package com.github.barteks2x.dodgeball;

import java.io.Serializable;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class LocationSerializable implements Serializable {

    private static final long serialVersionUID = 1L;
    public double x, y, z, yaw, pitch;
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

    public LocationSerializable(LocationSerializable l) {
        this(l.getLocation());
    }

    public Location getLocation() {
        return new Location(getWorldObj(), x, y, z);
    }

    public LocationSerializable add(Vector v) {
        this.x += v.getX();
        this.y += v.getY();
        this.z += v.getZ();
        return this;
    }

    public World getWorldObj() {
        if(worldObj == null) {
            this.worldObj = Plugin.plug.getServer().getWorld(world);
            if(worldObj == null) {
                Plugin.plug.getLogger().log(Level.WARNING, "Couldn''t find world: {0}", world);
            }
        }
        return worldObj;
    }
}

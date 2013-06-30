package org.barteks2x.minielementgaming;

import java.io.OutputStream;
import java.io.Serializable;
import org.bukkit.Location;

public class Arena implements Serializable {

	private static final long serialVersionUID = 342134832462L;
	private LocationSerializable minPoint, maxPoint;
	private Minigame minigame;

	public Arena(Location minPoint, Location maxPoint, Minigame mg) {
		this.minPoint = new LocationSerializable(minPoint);
		this.maxPoint = new LocationSerializable(maxPoint);
		this.minigame = mg;
	}

	public void serialize(OutputStream os) {
	}
}

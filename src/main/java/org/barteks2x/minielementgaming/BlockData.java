package org.barteks2x.minielementgaming;

public class BlockData {

	public final int id;
	public final byte meta;

	public BlockData(int id) {
		this(id, 0);
	}

	public BlockData(int id, int meta) {
		this.id = id;
		this.meta = (byte)meta;
	}
}

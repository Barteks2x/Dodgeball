package com.github.barteks2x.dodgeball;

import org.bukkit.Location;

public class BlockData {

    private static final long serialVersionUID = 4252365235623845L;
    public final int id;
    public final byte meta;
    public final Location loc;

    private static int getIdFromString(String idMeta) {
        String data;
        if(idMeta.contains(":")) {
            String[] s = idMeta.split(":");
            if(s.length == 0) {
                data = idMeta.replace(":", "");
            } else {
                data = s[0];
            }
        } else {
            data = idMeta;
        }
        return Integer.valueOf(data);
    }

    private static int getMetaFromString(String idMeta) {
        String data;
        if(idMeta.contains(":")) {
            String[] s = idMeta.split(":");
            if(s.length < 2) {
                data = idMeta.replace(":", "");
            } else {
                data = s[1];
            }
        } else {
            data = "0";
        }
        return Integer.valueOf(data);
    }

    public BlockData(int id) {
        this(id, 0);
    }

    public BlockData(int id, int meta) {
        this(id, meta, null);
    }

    public BlockData(int id, int meta, Location loc) {
        this.id = id;
        this.meta = (byte)meta;
        this.loc = loc;
    }

    public BlockData(String idMeta) {
        this(getIdFromString(idMeta), getMetaFromString(idMeta));
    }
}

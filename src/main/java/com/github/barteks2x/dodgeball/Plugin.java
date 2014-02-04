package com.github.barteks2x.dodgeball;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {
    public static Plugin plug;

    private WorldEditPlugin worldedit;
    private DodgeballManager mm;

    public static void main(String[] args) {
        Logger.getLogger(Plugin.class.getName()).info(
                "This is bukkit plugin! Copy it to plugins folder and run Bukkit.");
    }

    @Override
    public void onEnable() {
        plug = this;
        worldedit = (WorldEditPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        File dir = this.getDataFolder();
        if(dir.exists() && !dir.isDirectory()) {
            dir.delete();
        }
        dir.mkdirs();
        dir.mkdir();
        File data = new File(dir, "arenadata.bin");
        FileInputStream fis;
        ObjectInputStream ois = null;
        if(data.exists() && !data.isDirectory()) {
            try {
                fis = new FileInputStream(data);
                ois = new ObjectInputStream(fis);
                Object obj = ois.readObject();
                mm = (DodgeballManager)obj;
            } catch(StreamCorruptedException ex) {
                this.getLogger().log(Level.SEVERE, "Arena data corrupted!", ex);
            } catch(FileNotFoundException ex) {
                this.getLogger().log(Level.SEVERE, "Impossible exception!", ex);
            } catch(IOException ex) {
                this.getLogger().log(Level.SEVERE, "Couldn't read arena data!", ex);
            } catch(ClassNotFoundException ex) {
                this.getLogger().log(Level.SEVERE, "Arena data is corrupted!", ex);
            } catch(ClassCastException ex) {
                this.getLogger().log(Level.SEVERE, "Arena data is corrupted!", ex);
            } finally {
                if(ois != null) {
                    try {
                        ois.close();
                    } catch(IOException ex) {
                    }
                }
            }
        } else {
            try {
                if(data.exists()) {
                    data.delete();//Delete if exists (is directory)
                }
                data.createNewFile();
            } catch(IOException ex) {
                this.getLogger().log(Level.SEVERE, "Couldn't create arena data file!", ex);
            }
        }
        if(mm == null) {
            mm = new DodgeballManager();
        }
        mm.init(this);
        CommandExecutor exec = new MinigameCommandsAndListener(this, worldedit);
        Listener l = (Listener)exec;
        getServer().getPluginManager().registerEvents(l, this);
        getCommand("db").setExecutor(exec);
        getServer().getPluginManager().registerEvents(mm, this);
    }

    @Override
    public void onDisable() {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            File data = new File(this.getDataFolder(), "arenadata.bin");
            data.mkdirs();
            data.createNewFile();
            fos = new FileOutputStream(data);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(mm);
        } catch(FileNotFoundException ex) {
            this.getLogger().log(Level.SEVERE, "Impossible exception!", ex);
        } catch(IOException ex) {
            this.getLogger().log(Level.SEVERE, "Couldn't write arena data!", ex);
        } finally {
            try {
                if(fos != null) {
                    fos.close();
                }
                if(oos != null) {
                    oos.close();
                }
            } catch(IOException ex) {
            }
        }
    }

    public DodgeballManager getMinigameManager() {
        return mm;
    }
}

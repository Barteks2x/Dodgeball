package com.github.barteks2x.minielementgaming;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

	private WorldEditPlugin worldedit;
	private MinigameManager mm;

	@Override
	public void onEnable() {
		worldedit = (WorldEditPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
		CommandExecutor exec = new MinigameCommandsAndListener(this, worldedit);
		Listener l = (Listener)exec;
		getServer().getPluginManager().registerEvents(l, this);
		getCommand("db").setExecutor(exec);
		mm = new MinigameManager(this);
		getServer().getPluginManager().registerEvents(mm, this);
	}

	@Override
	public void onDisable() {
	}

	public static void main(String[] args) {
		Logger.getLogger(Plugin.class.getName()).info(
				"This is bukkit plugin! Copy it to plugins folder and run Bukkit.");
	}

	public MinigameManager getMinigameManager() {
		return mm;
	}
}

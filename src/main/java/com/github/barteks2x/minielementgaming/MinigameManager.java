package com.github.barteks2x.minielementgaming;

import com.github.barteks2x.minielementgaming.minigames.Minigame;
import java.util.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class MinigameManager implements Listener {

	private final Map<String, Minigame> minigames;
	private final Map<String, MinigamePlayer> players;
	private final Map<String, PlayerData> playersData;
	private final Plugin plug;

	public MinigameManager(Plugin plug) {
		this.playersData = new HashMap<String, PlayerData>(plug.getServer().getMaxPlayers());
		this.players = new HashMap<String, MinigamePlayer>(plug.getServer().getMaxPlayers());
		this.minigames = new HashMap<String, Minigame>(5);
		this.plug = plug;
	}

	public boolean addPlayer(MinigamePlayer p) {
		//Minigame is added to manager in constructor
		if (players.containsValue(p)) {
			return false;
		}
		Player pl = p.getPlayer();
		playersData.put(pl.getName(), new PlayerData(p));
		players.put(pl.getName(), p);
		pl.getInventory().clear();
		pl.getEquipment().clear();
		p.getMinigame().players.add(p);
		return true;
	}

	public boolean addMinigame(Minigame m) {
		if (minigames.containsValue(m)) {
			return false;
		}
		minigames.put(m.getName(), m);
		return true;
	}

	public boolean removePlayer(MinigamePlayer p) {
		if (!players.containsValue(p)) {
			return false;
		}
		p.getPlayer().teleport(p.getMinigame().getSpawn());
		playersData.get(p.getPlayer().getName()).restorePlayerData();
		players.remove(p.getPlayer().getName());
		p.getMinigame().players.remove(p);
		return true;
	}

	public boolean removeMinigame(Minigame m) {
		if (!minigames.containsValue(m)) {
			return false;
		}
		Iterator<MinigamePlayer> it = players.values().iterator();
		while (it.hasNext()) {
			MinigamePlayer mp = it.next();
			if (mp.getMinigame() == m) {
				removePlayer(mp);
			}
		}
		minigames.remove(m.getName());
		return true;
	}

	public MinigamePlayer getMinigamePlayer(String name) {
		return players.get(name);
	}

	public Minigame getPlayerMinigame(String name) {
		if (!players.containsKey(name)) {
			return null;
		}
		return players.get(name).getMinigame();
	}

	public Minigame getMinigame(String name) {
		return minigames.get(name);
	}

	public Iterator<Minigame> getMinigames() {
		return minigames.values().iterator();
	}

	public boolean hasPlayer(String name) {
		return players.containsKey(name);
	}

	public MinigamePlayer createPlayer(Player p, Minigame m) {
		return new MinigamePlayer(p, m.autoSelectTeam(), m);
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if (playersData.containsKey(e.getPlayer().getName())) {
			playersData.get(e.getPlayer().getName()).restorePlayerData();
			playersData.remove(e.getPlayer().getName());
		}
	}
}
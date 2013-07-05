package com.github.barteks2x.dodgeball;

import java.util.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class MinigameManager implements Listener {

	private final Map<String, Minigame> minigames;
	private final Map<String, DodgeballPlayer> players;
	private final Map<String, PlayerData> playersData;
	private final Plugin plug;

	public MinigameManager(Plugin plug) {
		this.playersData = new HashMap<String, PlayerData>(plug.getServer().getMaxPlayers());
		this.players = new HashMap<String, DodgeballPlayer>(plug.getServer().getMaxPlayers());
		this.minigames = new HashMap<String, Minigame>(5);
		this.plug = plug;
	}

	public boolean addPlayer(DodgeballPlayer p) {
		//Minigame is added to manager in constructor
		if (players.containsValue(p)) {
			return false;
		}
		Player pl = p.getPlayer();
		playersData.put(pl.getName(), new PlayerData(p));
		players.put(pl.getName(), p);
		pl.getInventory().clear();
		pl.getEquipment().clear();
		p.getMinigame().playerList.add(p);
		p.getMinigame().players++;
		return true;
	}

	public boolean addMinigame(Minigame m) {
		if (minigames.containsValue(m)) {
			return false;
		}
		minigames.put(m.getName(), m);
		return true;
	}

	public boolean removePlayer(DodgeballPlayer p) {
		if (!players.containsValue(p)) {
			return false;
		}
		Minigame m = p.getMinigame();
		if (m.getSpawn() != null) {
			p.getPlayer().teleport(p.getMinigame().getSpawn());
		}
		p.getMinigame().teamPlayerCount[p.getTeam().ordinal()]--;
		playersData.get(p.getPlayer().getName()).restorePlayerData();
		players.remove(p.getPlayer().getName());
		p.getMinigame().playerList.remove(p);
		p.getMinigame().players--;
		if (p.getMinigame().teamPlayerCount[0] == 0 || p.getMinigame().teamPlayerCount[1] == 0) {
			removeMinigame(p.getMinigame());
		}
		return true;
	}

	public boolean removeMinigame(Minigame m) {
		if (!minigames.containsValue(m)) {
			return false;
		}
		Iterator<DodgeballPlayer> it = players.values().iterator();
		while (it.hasNext()) {
			DodgeballPlayer mp = it.next();
			if (mp.getMinigame() == m) {
				removePlayer(mp);
			}
		}
		minigames.remove(m.getName());
		return true;
	}

	public DodgeballPlayer getMinigamePlayer(String name) {
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

	public DodgeballPlayer createPlayer(Player p, Minigame m, String team) {
		if (m.hasTeam(team)) {
			return new DodgeballPlayer(p, DodgeballTeam.valueOf(team), m);
		}
		return new DodgeballPlayer(p, m.autoSelectTeam(), m);
	}

	public DodgeballPlayer createPlayer(Player p, Minigame m) {
		return createPlayer(p, m, null);
	}

	public void setPlayerSpactate(DodgeballPlayer p) {
		p.isSpectator = true;
		p.getMinigame().players--;
		if (p.getMinigame().teamPlayerCount[0] == 0 || p.getMinigame().teamPlayerCount[1] == 0) {
			removeMinigame(p.getMinigame());
		}
	}
}

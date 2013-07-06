package com.github.barteks2x.dodgeball;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class MinigameManager implements Listener {

	private final Map<String, Minigame> minigames;
	private final Map<String, DodgeballPlayer> players;
	private final Map<String, PlayerData> playersData;
	private final Plugin plug;
	private final Random rand = new Random();

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
		final Minigame m = p.getMinigame();
		playersData.put(pl.getName(), new PlayerData(p));
		players.put(pl.getName(), p);
		pl.getInventory().clear();
		pl.getEquipment().clear();
		m.playerList.add(p);
		m.players++;
		m.setPlayerAtRandomLocation(pl);
		if (m.playerList.toArray().length >= m.maxPlayers) {
			new BukkitRunnable() {
				public void run() {
					m.onStart();
				}
			}.runTaskLater(plug, 20 * 30);
		} else {
			pl.sendMessage(ChatColor.GOLD +
					"Use /db vote to vote on starting minigame before it's full.");
		}
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
		Player pl = p.getPlayer();
		if (m.getSpawn() != null) {
			pl.teleport(m.getSpawn());
		}
		m.teamPlayerCount[p.getTeam().ordinal()]--;
		playersData.get(pl.getName()).restorePlayerData();
		players.remove(pl.getName());
		m.playerList.remove(p);
		m.players--;
		if (m.teamPlayerCount[0] == 0 || m.teamPlayerCount[1] == 0) {
			stopMinigame(m);
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
		final Minigame m = p.getMinigame();
		if (m.teamPlayerCount[0] == 0 || m.teamPlayerCount[1] == 0) {
			stopMinigame(m);
		}
	}

	public void stopMinigame(String name) {
		if (minigames.containsKey(name)) {
			stopMinigame(minigames.get(name));
		}
	}

	public void vote(final Minigame m) {
		m.votes++;
		if (m.votes >= .5F * m.maxPlayers) {
			new BukkitRunnable() {
				public void run() {
					m.onStart();
				}
			}.runTaskLater(plug, 30 * 20);
		}
	}

	private void stopMinigame(final Minigame m) {
		if (!m.isStarted) {
			return;
		}

		new BukkitRunnable() {
			private void stopMinigame_exec(Minigame m) {
				m.onStop();
				int[] teams = m.teamPlayerCount;
				int[] ateams = new int[2];
				int n = 0;
				for (int i = 0; i < teams.length; ++i) {
					if (n == 2) {
						break;
					}
					if (m.hasTeam(DodgeballTeam.values()[i].toString())) {
						ateams[n++] = i;
					}
				}
				DodgeballTeam winnerTeam = teams[ateams[0]] < teams[ateams[1]] ?
						DodgeballTeam.values()[ateams[1]] : (teams[ateams[0]] == teams[ateams[1]] ?
						null : DodgeballTeam.values()[ateams[0]]);
				List<DodgeballPlayer> players = m.playerList;
				for (DodgeballPlayer p : players) {
					if (winnerTeam == null || p.getTeam() == winnerTeam) {
						p.getPlayer().sendMessage(ChatColor.GOLD + "Your team wins!");
					}
					removePlayer(p);
				}

				int fireworks = rand.nextInt(50) + 30;
				for (int i = 0; i < fireworks; ++i) {
					new FireworkEffectTask(rand.nextLong(), m.area).runTaskLater(plug, rand.nextInt(
							100) + 101);
				}
			}

			public void run() {
				stopMinigame_exec(m);
			}
		}.runTaskLater(plug, 60);
	}

	private FireworkEffect getRandomFireworkEffect() {
		FireworkEffect.Builder b = FireworkEffect.builder();
		FireworkEffect.Type types[] = FireworkEffect.Type.values();
		return b.trail(true).with(types[rand.nextInt(types.length)]).with(types[rand.nextInt(
				types.length)]).withColor(Color.fromRGB(rand.nextInt(255), rand.nextInt(255), rand.
				nextInt(255))).withFade(Color.fromRGB(rand.nextInt(255), rand.nextInt(255), rand.
				nextInt(255))).build();
	}

	private class FireworkEffectTask extends BukkitRunnable {

		private CubeSerializable c;
		private Random rand;

		public FireworkEffectTask(Long rand, CubeSerializable cube) {
			this.rand = new Random(rand);
			this.c = cube;
		}

		public void run() {
			int minX = (int)c.minPoint.x;
			int minZ = (int)c.minPoint.z;
			int maxX = (int)c.maxPoint.x;
			int maxZ = (int)c.maxPoint.z;

			int randX = rand.nextInt(maxX - minX - 2) + minX + 1;
			int randY = (int)(rand.nextInt(10) + c.maxPoint.y);
			int randZ = rand.nextInt(maxZ - minZ - 2) + minZ + 1;
			World w = c.minPoint.worldObj;
			try {
				FireworkEffectPlayer.playFirework(w, new Location(w, randX, randY, randZ),
						getRandomFireworkEffect());
			} catch (Exception ex) {
				Logger.getLogger(MinigameManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}
}

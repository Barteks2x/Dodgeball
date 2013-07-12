package com.github.barteks2x.dodgeball;

import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import static com.github.barteks2x.dodgeball.Plugin.plug;

public class DodgeballManager implements Listener, Serializable {

	private static final long serialVersionUID = 3243645834925L;
	private final HashMap<String, Dodgeball> minigames;
	private transient HashMap<String, DodgeballPlayer> players;
	private transient HashMap<String, PlayerData> playersData;
	private final Random rand = new Random();
	private LocationSerializable globalSpawn;

	public DodgeballManager() {
		this.minigames = new HashMap<String, Dodgeball>(5);
	}

	public DodgeballManager init(Plugin plug) {
		this.playersData = new HashMap<String, PlayerData>(plug.getServer().getMaxPlayers() + 5);
		this.players = new HashMap<String, DodgeballPlayer>(plug.getServer().getMaxPlayers() + 5);
		Collection<Dodgeball> c = minigames.values();
		for (Dodgeball db : c) {
			db.reinit();
		}
		return this;
	}

	public boolean addPlayer(DodgeballPlayer p) {
		//Minigame is added to manager in constructor
		if (players.containsValue(p)) {
			return false;
		}

		Player pl = p.getPlayer();
		final Dodgeball m = p.getMinigame();
		playersData.put(pl.getName(), new PlayerData(p));
		players.put(pl.getName(), p);
		pl.getInventory().clear();
		pl.getEquipment().clear();
		m.onPlayrJoin(p);
		m.setPlayerAtRandomLocation(pl);
		if (m.playerList.toArray().length >= m.maxPlayers - 10) {
			startMinigameDelayed(m);
		} else {
			pl.sendMessage(ChatColor.GOLD +
					"Use /db vote to vote on starting minigame before it's full.");
		}
		return true;
	}

	public boolean addMinigame(Dodgeball m) {
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
		Dodgeball m = p.getMinigame();
		Player pl = p.getPlayer();
		if (m.getSpawn() != null) {
			pl.teleport(m.getSpawn());
		}
		playersData.get(pl.getName()).restorePlayerData();
		players.remove(pl.getName());
		m.onPlayerLeave(p);
		return true;
	}

	public boolean removeMinigame(Dodgeball m) {
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

	public Dodgeball getPlayerMinigame(String name) {
		if (!players.containsKey(name)) {
			return null;
		}
		return players.get(name).getMinigame();
	}

	public Dodgeball getMinigame(String name) {
		return minigames.get(name);
	}

	public Iterator<Dodgeball> getMinigames() {
		return minigames.values().iterator();
	}

	public boolean hasPlayer(String name) {
		return players.containsKey(name);
	}

	public DodgeballPlayer createPlayer(Player p, Dodgeball m, String team) {
		if (m.hasTeam(team)) {
			return new DodgeballPlayer(p, DodgeballTeam.valueOf(team), m);
		}
		return new DodgeballPlayer(p, m.autoSelectTeam(), m);
	}

	public DodgeballPlayer createPlayer(Player p, Dodgeball m) {
		return createPlayer(p, m, null);
	}

	public void stopMinigame(String name) {
		if (minigames.containsKey(name)) {
			stopMinigame(minigames.get(name));
		}
	}

	public void vote(final Dodgeball m) {
		m.votes++;
		if (m.votes >= Math.sqrt(m.maxPlayers) * Math.log10(m.maxPlayers) || m.votes >=
				m.maxPlayers / 4) {
			startMinigameDelayed(m);
		}
	}

	void stopMinigame(final Dodgeball m) {
		if (!m.canStop()) {
			return;
		}
		m.preStop();
		final DodgeballTeam winnerTeam = m.getWinnerTeam();
		final DodgeballPlayer[] pList = new DodgeballPlayer[m.playerList.toArray().length + 1];
		m.playerList.toArray(pList);
		for (DodgeballPlayer p : pList) {
			if (p == null) {
				continue;
			}
			p.isSpectator = true;
		}
		int fireworks = 10;
		for (int i = 0; i < fireworks; ++i) {
			new FireworkEffectTask(rand.nextLong(), m.area).runTaskLater(plug,
					rand.nextInt(8 * 20) + 2 * 20);
		}
		new BukkitRunnable() {
			public void run() {
				for (DodgeballPlayer p : pList) {
					if (p == null) {
						continue;
					}
					removePlayer(p);
				}
				m.onStop();
			}
		}.runTaskLater(plug, 10 * 20);

		new BukkitRunnable() {
			public void run() {
				for (DodgeballPlayer p : pList) {
					if (p == null) {
						continue;
					}
					if (winnerTeam != null) {
						p.getPlayer().sendMessage(ChatColor.GOLD + "Winner team: " + winnerTeam.
								getColor() + winnerTeam.toString().toLowerCase());
					}
				}
			}
		}.runTaskLater(plug, 30);
	}

	public void startMinigameDelayed(final Dodgeball m) {
		if (!m.canStart()) {
			return;
		}
		m.preStart();
		new BukkitRunnable() {
			public void run() {
				new StartMinigameTask(m).runTaskLater(plug, 30 * 20);
				new SendMessageTask(ChatColor.DARK_RED + "30 seconds to start minigame...").runTask(
						plug);
				new SendMessageTask(ChatColor.RED + "20 seconds to start minigame...").runTaskLater(
						plug, 10 * 20);
				new SendMessageTask(ChatColor.YELLOW + "15 seconds to start minigame...").
						runTaskLater(plug, 15 * 20);
				for (int i = 10; i > 0; --i) {
					new SendMessageTask(ChatColor.GREEN + String.valueOf(i) +
							" seconds to start minigame...").runTaskLater(plug,
							(30 * 20) - i * 20);
				}
			}

			final class SendMessageTask extends BukkitRunnable {

				String message;

				public SendMessageTask(String message) {
					this.message = message;
				}

				public void run() {
					for (DodgeballPlayer p : m.playerList) {
						p.getPlayer().sendMessage(message);
					}
				}
			}

			final class StartMinigameTask extends BukkitRunnable {

				Dodgeball db;

				public StartMinigameTask(Dodgeball db) {
					this.db = db;
				}

				public void run() {
					this.db.onStart();
				}
			}
		}.runTask(plug);
	}

	private class FireworkEffectTask extends BukkitRunnable {

		private CubeSerializable c;
		private Random rand;

		public FireworkEffectTask(Long rand, CubeSerializable cube) {
			this.rand = new Random(rand);
			this.c = cube;
		}

		private FireworkEffect getRandomFireworkEffect() {
			FireworkEffect.Builder b = FireworkEffect.builder();
			FireworkEffect.Type types[] = FireworkEffect.Type.values();
			return b.trail(rand.nextBoolean()).with(types[rand.nextInt(types.length)]).with(
					types[rand.nextInt(types.length)]).withColor(Color.fromRGB(rand.nextInt(255),
					rand.nextInt(255), rand.nextInt(255))).withFade(Color.fromRGB(rand.nextInt(255),
					rand.nextInt(255), rand.nextInt(255))).flicker(rand.nextBoolean()).build();
		}

		public void run() {
			int minX = (int)c.minPoint.x;
			int minZ = (int)c.minPoint.z;
			int maxX = (int)c.maxPoint.x;
			int maxZ = (int)c.maxPoint.z;

			int randX = rand.nextInt(maxX - minX - 2) + minX + 1;
			int randY = (int)(rand.nextInt(10) + c.maxPoint.y);
			int randZ = rand.nextInt(maxZ - minZ - 2) + minZ + 1;
			World w = c.minPoint.getWorldObj();
			try {
				FireworkEffectPlayer.playFirework(w, new Location(w, randX, randY, randZ),
						getRandomFireworkEffect(), rand.nextInt(3) + 1);
			} catch (Exception ex) {
				Logger.getLogger(DodgeballManager.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public void setGlobalSpawn(Location l) {
		globalSpawn = new LocationSerializable(l);
	}

	public LocationSerializable getGlobalSpawn() {
		return globalSpawn;
	}
}

package com.github.barteks2x.dodgeball;

import com.github.barteks2x.dodgeball.command.*;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class MinigameCommandsAndListener implements CommandExecutor, Listener {

	private final Plugin plugin;
	private final WorldEditPlugin worldedit;
	private final MinigameManager mm;
	private final Map<String, Object> commands = new HashMap<String, Object>(15);

	public MinigameCommandsAndListener(Plugin plugin, WorldEditPlugin worldedit) {
		this.plugin = plugin;
		this.worldedit = worldedit;
		this.mm = plugin.getMinigameManager();
		this.registerCommands();
	}

	private void registerCommands() {
		registerCommand(new ReloadConfig(mm));
		registerCommand(new KickPlayer(mm));
		registerCommand(new Automake(mm, plugin, worldedit));
		registerCommand(new Spectate(mm));
		registerCommand(new Delete(mm));
		registerCommand(new Spawn(mm));
		registerCommand(new Leave(mm));
		registerCommand(new Start(mm));
		registerCommand(new Join(mm));
		registerCommand(new Help(mm));
		registerCommand(new Stop(mm));
		registerCommand(new Vote(mm));
	}

	public void registerCommand(Object obj) {
		Method methods[] = obj.getClass().getDeclaredMethods();
		for (Method m : methods) {
			if (m.isAnnotationPresent(DBCommand.class)) {
				Class<?> params[] = m.getParameterTypes();
				if (params.length == 2 && params[0] == CommandSender.class && params[1] ==
						Iterator.class && m.getReturnType() == Boolean.TYPE) {
					String name = m.getName().toLowerCase();
					commands.put(name, obj);
				}
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> paramList = new LinkedList<String>();
		Utils.addArrayToList(paramList, args);
		Iterator<String> it = paramList.iterator();
		if ("db".equalsIgnoreCase(cmd.getName())) {
			if (!it.hasNext()) {
				return false;
			}
			String par = it.next().toLowerCase();
			if (!commands.containsKey(par)) {
				return false;
			}
			return executeCommand(par, sender, it);
		}
		return false;
	}

	private boolean executeCommand(String cmd, CommandSender sender,
			Iterator<String> args) {
		try {
			return (Boolean)commands.get(cmd).getClass().getMethod(cmd, CommandSender.class,
					Iterator.class).invoke(commands.get(cmd), sender, args);
		} catch (SecurityException ex) {
			Logger.getLogger(MinigameCommandsAndListener.class.getName()).
					log(Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(MinigameCommandsAndListener.class.getName()).
					log(Level.SEVERE, null, ex);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(MinigameCommandsAndListener.class.getName()).
					log(Level.SEVERE, null, ex);
		} catch (InvocationTargetException ex) {
			Logger.getLogger(MinigameCommandsAndListener.class.getName()).
					log(Level.SEVERE, null, ex);
		} catch (NullPointerException ex) {
			Logger.getLogger(MinigameCommandsAndListener.class.getName()).
					log(Level.SEVERE, null, ex);
		} catch (ClassCastException ex) {
			Logger.getLogger(MinigameCommandsAndListener.class.getName()).
					log(Level.SEVERE, null, ex);
		} catch (NoSuchMethodException ex) {
			Logger.getLogger(MinigameCommandsAndListener.class.getName()).
					log(Level.SEVERE, null, ex);
		}
		return false;
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent e) {
		Minigame ab = mm.getPlayerMinigame(e.getPlayer().getName());
		if (ab == null) {
			return;
		}
		ab.handlePlayerMove(e);
	}

	@EventHandler()
	public void onPlayerInteract(PlayerInteractEvent e) {
		Block b = e.getClickedBlock();
		if (b == null || !(b.getState() instanceof Sign)) {
			return;
		}
		Sign s = (Sign)b.getState();
		String lines[] = s.getLines();
		if (!"[dodgeball]".equalsIgnoreCase(lines[0]) || lines[1] == null || lines[1].length() == 0) {
			return;
		}
		if (!lines[1].startsWith("[") || !lines[1].endsWith("]")) {
			return;
		}
		final String name = lines[1].replace("[", "").replace("]", "");
		if (name.trim().length() == 0) {
			return;
		}
		Player p = e.getPlayer();
		executeCommand("join", p, new Iterator<String>() {
			public boolean hasNext() {
				return true;//Always has next
			}

			public String next() {
				return name;//Always return the same String
			}

			public void remove() {//Do not remove
			}
		});
	}

	@EventHandler()
	public void onEntityDamageEntity(EntityDamageByEntityEvent e) {
		EntityDamageByEntityHandler eh = new EntityDamageByEntityHandler(e);
		eh.runTask(plugin);
	}

	@EventHandler()
	public void onProjectileHit(ProjectileHitEvent e) {
		Iterator<Minigame> it = mm.getMinigames();
		while (it.hasNext()) {
			it.next().handleProjectileHitEvent(e);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent e) {
		HumanEntity human = e.getWhoClicked();
		if (!(human instanceof Player)) {
			return;
		}
		Player p = (Player)human;
		Minigame m;
		if ((m = mm.getPlayerMinigame(p.getName())) == null) {
			return;
		}
		m.handlePlayerInventoryClick(e);
	}

	private class EntityDamageByEntityHandler extends BukkitRunnable {

		private EntityDamageByEntityEvent e;

		public EntityDamageByEntityHandler(EntityDamageByEntityEvent e) {
			this.e = e;
		}

		public void run() {
			if (e.getEntity() instanceof Player) {
				if (e.getDamager() instanceof Snowball) {
					Player p = (Player)e.getEntity();
					Minigame m = mm.getPlayerMinigame(p.getName());
					if (m != null) {
						m.handleEntityDamageByEntity(e);
					}
				}
			}
		}
	}
}

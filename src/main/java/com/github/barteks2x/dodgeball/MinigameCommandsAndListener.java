package com.github.barteks2x.dodgeball;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import java.util.*;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MinigameCommandsAndListener implements CommandExecutor, Listener {

	private final Plugin plugin;
	private final WorldEditPlugin worldedit;
	private final int minArenaSizeXZ = 10;
	private final int minArenaSizeY = 4;
	private final String noargsmsg =
			"Not enough parameters!\n/minigame help <command1> <command2> ... for more info";
	private final String noselectionmsg = "Nothing selected. You must select arena!";
	private final String toosmallarenamsg = "Too small arena! Arena must be at least " +
			minArenaSizeXZ + "x" + minArenaSizeY + "x" + minArenaSizeXZ;
	private Minigame selectedArena;
	private final MinigameManager mm;

	public MinigameCommandsAndListener(Plugin plugin, WorldEditPlugin worldedit) {
		this.plugin = plugin;
		this.worldedit = worldedit;
		this.mm = plugin.getMinigameManager();
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Minigame ab = mm.getPlayerMinigame(e.getPlayer().getName());
		if (ab == null) {
			return;
		}
		ab.handlePlayerMove(e);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		//TODO onPlayerInteract
	}

	@EventHandler
	public void onEntityDamageEntity(EntityDamageByEntityEvent e) {
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

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent e) {
		Iterator<Minigame> it = mm.getMinigames();
		while (it.hasNext()) {
			it.next().handleProjectileHitEvent(e);
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> paramList = new LinkedList<String>();
		Utils.addArrayToList(paramList, args);
		Iterator<String> it = paramList.iterator();
		if ("db".equalsIgnoreCase(cmd.getName())) {
			return minigame(sender, it);
		}
		if ("dbhelp".equalsIgnoreCase(cmd.getName())) {
			return help(sender, it);
		}
		return false;
	}

	private boolean join(CommandSender sender, Iterator<String> args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command must be executed by player");
			return true;
		}
		if (!args.hasNext()) {
			sender.sendMessage("No minigame specified!");
			return true;
		}
		String n = args.next();
		String p = ((Player)sender).getName();
		if (mm.hasPlayer(p)) {
			sender.sendMessage("You can't join two minigames! Use /leave to leave current minigame");
			return true;
		}
		Minigame m = mm.getMinigame(n);
		if (m == null) {
			sender.sendMessage(
					"Minigame not exist! Are you sure name is correct?\"Mini\" and \"MiNi\" are different minigames!");
			return true;
		}
		mm.addPlayer(mm.createPlayer((Player)sender, m));
		sender.sendMessage("Joined to minigame: " + n);
		return true;
	}

	private boolean minigame(CommandSender sender, Iterator<String> args) {
		if (!args.hasNext()) {
			return false;
		}
		String par = args.next();
		if ("reload-config".equalsIgnoreCase(par) || "rc".equalsIgnoreCase(par)) {
			return reloadConfig(sender);
		}
		if ("automake".equalsIgnoreCase(par)) {
			return autobuild(sender, args);
		}
		if ("create".equalsIgnoreCase(par) || "c".equalsIgnoreCase(par)) {
			return create(sender, args);
		}
		if ("delete".equalsIgnoreCase(par) || "d".equalsIgnoreCase(par)) {
			return delete(sender, args);
		}
		if ("start".equalsIgnoreCase(par) || "st".equalsIgnoreCase(par)) {
			return start(sender, args);
		}
		if ("stop".equalsIgnoreCase(par) || "sp".equalsIgnoreCase(par)) {
			return stop(sender, args);
		}
		if ("kickplayer".equalsIgnoreCase(par) || "kp".equalsIgnoreCase(par)) {
			return kickPlayer(sender, args);
		}
		if ("join".equalsIgnoreCase(par)) {
			return join(sender, args);
		}
		if ("leave".equalsIgnoreCase(par)) {
			return leave(sender);
		}
		if ("help".equalsIgnoreCase(par)) {
			help(sender, args);
		}
		if ("save".equalsIgnoreCase(par)) {
			savestate(sender);
		}
		if ("setspawn".equalsIgnoreCase(par)) {
			return setSpawn(sender, args);
		}
		return false;
	}

	private boolean leave(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command must be used by player");
			return true;
		}
		mm.removePlayer(mm.getMinigamePlayer(((Player)sender).getName()));
		sender.sendMessage("Left ciurrent minigame.");
		//TODO tp to spawn after minigame
		return true;
	}

	private boolean help(CommandSender sender, Iterator<String> args) {
		return false;//TODO help
	}

	private boolean reloadConfig(CommandSender sender) {
		//TODO Auto-generated method
		return false;
	}

	private boolean create(CommandSender sender, Iterator<String> args) {
		//TODO Auto-generated method
		return false;
	}

	private boolean delete(CommandSender sender, Iterator<String> args) {
		//TODO Auto-generated method
		return false;
	}

	private boolean start(CommandSender sender, Iterator<String> args) {
		if (!args.hasNext()) {
			sender.sendMessage(noargsmsg);
			return true;
		}
		String name = args.next();
		Minigame m = mm.getMinigame(name);
		m.onStart();
		return false;
	}

	private boolean stop(CommandSender sender, Iterator<String> args) {
		//TODO Auto-generated method
		return false;
	}

	private boolean kickPlayer(CommandSender sender, Iterator<String> args) {
		//TODO Auto-generated method
		return false;
	}

	private boolean select(CommandSender sender, Iterator<String> args) {
		//TODO Auto-generated method
		return false;
	}

	private boolean savestate(CommandSender sender) {
		//TODO Auto-generated method
		return false;
	}

	private boolean rebuild(CommandSender sender) {
		//TODO Auto-generated method
		return false;
	}

	private boolean autobuild(CommandSender sender, Iterator<String> args) {
		if (!args.hasNext()) {
			sender.sendMessage(noargsmsg);
			return true;
		}
		String teamsString = args.next();
		String teams[] = teamsString.split(":");
		if (teams.length != 2) {
			sender.sendMessage("Wrong paraneter: " + teamsString);
			return true;
		}
		DodgeballTeam team1, team2;
		try {
			team1 = DodgeballTeam.valueOf(teams[0]);
			team2 = DodgeballTeam.valueOf(teams[1]);
		} catch (IllegalArgumentException ex) {
			sender.sendMessage("Incorrect color");
			return true;
		}

		Selection sel = worldedit.getSelection((Player)sender);
		if (sel == null) {
			sender.sendMessage(noselectionmsg);
			return true;
		}
		if (sel.getHeight() < minArenaSizeY || sel.getWidth() < minArenaSizeXZ || sel.
				getLength() < minArenaSizeXZ) {
			sender.sendMessage(toosmallarenamsg);
			return true;
		}
		Location maxPoint = sel.getMaximumPoint();
		Location minPoint = sel.getMinimumPoint();
		if (!args.hasNext()) {
			sender.sendMessage(noargsmsg);
			return true;
		}
		String name = args.next();
		BlockData[] blocks = new BlockData[4];
		for (int i = 0; i < blocks.length; ++i) {
			if (!args.hasNext()) {
				sender.sendMessage(noargsmsg);
				return true;
			}
			String par = args.next();
			try {
				blocks[i] = new BlockData(par);
			} catch (Exception ex) {
				sender.sendMessage("Wrong paraneter: " + par);
				return true;
			}
		}
		int sectionHeight;
		if (!args.hasNext()) {
			sender.sendMessage(noargsmsg);
			return true;
		}
		String par = args.next();
		try {
			sectionHeight = Integer.valueOf(par);
		} catch (Exception ex) {
			sender.sendMessage("Wrong parameter: " + par);
			return true;
		}
		ArenaCreator creator = new ArenaCreator(minPoint, maxPoint, blocks[0],
				blocks[1], blocks[2], blocks[3], sectionHeight, plugin);
		creator.runTaskAsynchronously(plugin);//avoid lag when command is executed
		Minigame arena = new Dodgeball(plugin, minPoint, maxPoint, name, team1, team2);
		selectedArena = arena;
		mm.addMinigame(arena);
		sender.sendMessage("Building dodgeball arena...");
		return true;
	}

	private boolean setSpawn(CommandSender sender, Iterator<String> args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can be used only by player");
			return true;
		}
		if (!args.hasNext()) {
			sender.sendMessage("No minigame name specified!");
			return true;
		}
		String name = args.next();
		mm.getMinigame(name).setSpawn(((Player)sender).getLocation());
		sender.sendMessage("Dodgeball spawn set: " + ((Player)sender).getLocation().toString());
		return true;
	}
}

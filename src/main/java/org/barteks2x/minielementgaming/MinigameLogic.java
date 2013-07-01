package org.barteks2x.minielementgaming;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import java.util.*;
import java.util.logging.Level;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MinigameLogic implements CommandExecutor, Listener {

	private final Plugin plugin;
	private final WorldEditPlugin worldedit;
	private final int minArenaSizeXZ = 10;
	private final int minArenaSizeY = 4;
	private final String noargsmsg =
			"Not enough parameters!\n/minigame help <command1> <command2> ... for more info";
	private final String noselectionmsg = "Nothing selected. You must select arena!";
	private final String toosmallarenamsg = "Too small arena! Arena must be at least " +
			minArenaSizeXZ + "x" + minArenaSizeY + "x" + minArenaSizeXZ;
	public final Map<String, ArenaBase> minigames = new HashMap<String, ArenaBase>();
	public final Map<String, String> players = new HashMap<String, String>();
	private ArenaBase selectedArena;

	MinigameLogic(Plugin plugin, WorldEditPlugin worldedit) {
		this.plugin = plugin;
		this.worldedit = worldedit;
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {

		String a = players.get(e.getPlayer().getName());
		if (a == null) {
			return;
		}
		ArenaBase ab = minigames.get(a);
		if (ab == null) {
			plugin.getLogger().log(Level.WARNING, "Error! Couldn't find player in minigame arena!");
			return;
		}
		ab.handlePlayerMove(e);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		//TODO onPlayerInteract
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> paramList = new LinkedList<String>();
		Utils.addArrayToList(paramList, args);
		Iterator<String> it = paramList.iterator();
		if ("meg".equalsIgnoreCase(cmd.getName())) {
			return minigame(sender, it);
		}
		if ("join".equalsIgnoreCase(cmd.getName())) {
			return join(sender, it);
		}
		if ("leave".equalsIgnoreCase(cmd.getName())) {
			return leave(sender);
		}
		if ("mghelp".equalsIgnoreCase(cmd.getName())) {
			return help(sender, it);
		}
		return false;
	}

	private boolean join(CommandSender sender, Iterator<String> args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command must be executed by player");
			return true;
		}
		players.put(((Player)sender).getName(), "example");
		minigames.get("example").addPlayer((Player)sender);
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
		if ("arena".equalsIgnoreCase(par)) {
			return arena(sender, args);
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
		return false;
	}

	private boolean leave(CommandSender sender) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command must be executed by player");
			return true;
		}
		players.remove(((Player)sender).getName());
		minigames.get("example").removePlayer((Player)sender);
		return true;
	}

	private boolean help(CommandSender sender, Iterator<String> args) {
		return false;//TODO help
	}

	private boolean arena(CommandSender sender, Iterator<String> args) {
		if (!args.hasNext()) {
			sender.sendMessage(new String[]{"Usage: /minigame arena <command>\n",
				"Avalble arena commands: autobuild, select, savestate, rebuild\n",
				"/minigame halp arena for more information"});
			return true;
		}
		String par = args.next();
		if ("autobuild".equalsIgnoreCase(par)) {
			return autobuild(sender, args);
		}
		if ("select".equalsIgnoreCase(par)) {
			return select(sender, args);
		}
		if ("savestate".equalsIgnoreCase(par)) {
			return savestate(sender);
		}
		if ("rebuild".equalsIgnoreCase(par) || "loadstate".equalsIgnoreCase(par)) {
			return rebuild(sender);
		}
		return false;
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
		//TODO Auto-generated method
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

	private boolean autobuild(CommandSender sender, Iterator<String> args) {
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
				blocks[1], blocks[2], blocks[3], sectionHeight);
		creator.runTaskLater(plugin, 1);//avoid lag when command is executed
		ArenaBase arena = new DodgeBallArena(minPoint, maxPoint);
		selectedArena = arena;
		minigames.put("example", arena);
		return true;
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
}

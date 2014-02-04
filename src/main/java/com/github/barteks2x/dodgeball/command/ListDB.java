package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.Dodgeball;
import com.github.barteks2x.dodgeball.DodgeballManager;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ListDB {

    DodgeballManager mm;

    public ListDB(DodgeballManager mm) {
        this.mm = mm;
    }

    @DBCommand
    public boolean list(CommandSender sender, Iterator<String> args) {
        if(!sender.hasPermission("db.list")) {
            sender.sendMessage(ChatColor.DARK_RED
                            + "You don't have permission to use this command!");
            return true;
        }
        int page = 1;
        if(args.hasNext()) {
            try {
                page = Integer.parseInt(args.next());
            } catch(NumberFormatException ex) {
                sender.sendMessage(ChatColor.RED + "Not a number!");
            }
        }
        int starti = (page - 1) * 10;
        int stopi = page * 10;
        Iterator<Dodgeball> it = mm.getMinigames();
        int i = 0;
        Dodgeball d;
        sender.sendMessage(ChatColor.GREEN + "Dodgeball list, page " + page);
        while(it.hasNext()) {
            d = it.next();
            if(i >= starti) {
                ChatColor c = d.canJoin() ? d.isPerStart() ? ChatColor.AQUA : ChatColor.GREEN
                        : ChatColor.RED;
                StringBuilder message = new StringBuilder(c + "Name: ");
                message.append(d.getName());
                message.append("  Players: ").append(d.players);
                message.append("  Can join: ").append(d.canJoin() ? "yes" : "no");
                message.append(" ").append(d.TEAM_1.toString().toLowerCase()).append(": ").append(d.
                        getTeamPlayers(d.TEAM_1));
                message.append(" ").append(d.TEAM_2.toString().toLowerCase()).append(": ").append(d.
                        getTeamPlayers(d.TEAM_2));
                sender.sendMessage(message.toString());
            }
            if(i >= stopi) {
                sender.sendMessage(ChatColor.BLUE + "Use /db list " + (page + 1) + " to see more.");
                return true;
            }
            ++i;
        }
        sender.sendMessage(ChatColor.YELLOW + "End of list.");
        return true;
    }
}

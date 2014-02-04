package com.github.barteks2x.dodgeball.command;

import com.github.barteks2x.dodgeball.DodgeballManager;
import java.util.Iterator;
import org.bukkit.command.CommandSender;

public class Help {

    private final DodgeballManager mm;

    public Help(DodgeballManager mm) {
        this.mm = mm;
    }

    @DBCommand
    public boolean help(CommandSender sender, Iterator<String> args) {
        return false;//Will send defalut help message to player
    }

    @DBCommand
    public boolean h(CommandSender sender, Iterator<String> args) {
        return this.help(sender, args);
    }
}

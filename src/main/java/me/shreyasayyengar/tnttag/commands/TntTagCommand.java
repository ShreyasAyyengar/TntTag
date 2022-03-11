package me.shreyasayyengar.tnttag.commands;

import me.shreyasayyengar.tnttag.arena.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TntTagCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        new Arena().addPlayer(((Player) sender));

        return false;
    }
}

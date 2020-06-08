package de.fileinputstream.mytraz.worldmanagement.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Alexander on 08.06.2020 21:16
 * © 2020 Alexander Fiedler
 */
public class CommandSethome implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

        }
        return false;
    }
}

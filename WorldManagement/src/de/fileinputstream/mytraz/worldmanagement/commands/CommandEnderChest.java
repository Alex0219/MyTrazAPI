package de.fileinputstream.mytraz.worldmanagement.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Alexander on 08.06.2020 16:15
 * © 2020 Alexander Fiedler
 */
public class CommandEnderChest implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.openInventory(player.getEnderChest());
        }
        return false;
    }
}

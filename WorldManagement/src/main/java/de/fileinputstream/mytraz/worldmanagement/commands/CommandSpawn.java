package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Alexander on 09.08.2020
 * © 2020 Alexander Fiedler
 **/
public class CommandSpawn implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            player.teleport(Bootstrap.getInstance().getSpawnLocation());
        }
        return false;
    }
}

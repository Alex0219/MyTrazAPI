package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Alexander on 10.08.2020
 * © 2020 Alexander Fiedler
 **/
public class CommandTpaccept implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    if (!Bootstrap.getInstance().getTpaInvites().containsKey(player.getName())) {
                        player.sendMessage("§bMC-Survival.de §7» §7Du hast " + target.getDisplayName() + " eine TPA Anfrage gesendet.");
                        player.sendMessage("§bMC-Survival.de §7» §7Diese wird in 2 Minuten ablaufen");
                        Bootstrap.getInstance().getTpaInvites().put(player.getName(), target.getName());
                    } else {
                        player.sendMessage("§bMC-Survival.de §7» §cDu hast diesem Spieler bereits eine TPA Anfrage gesendet.");
                        return true;
                    }
                } else {
                    player.sendMessage("§bMC-Survival.de §7» §cDer Spieler konnte nicht gefunden werden.");
                    return true;
                }
            } else {
                player.sendMessage("§bMC-Survival.de §7» Verwende /tpa <Spieler>");
            }
        }
        return false;
    }
}

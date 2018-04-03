package de.fileinputstream.mytraz.bungee.commands;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class Kick
        extends Command {
    public Kick() {
        super("kick");
    }


    public void execute(CommandSender p, String[] args) {
        if (!p.hasPermission("server.kick")) {
            return;
        }
        if (args.length < 2) {
            p.sendMessage("§bCloud §7 §cVerwende /kick <Spieler> <Grund>");
            return;
        }

        ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);


        target.disconnect("§7Du wurdest vom §cMyTraz §7Netzwerk gekickt! \n§eGrund: §c " + args[1]);


        for (ProxiedPlayer team : BungeeCord.getInstance().getPlayers()) {
            if (team.hasPermission("server.team")) {
                team.sendMessage("§7§m-------------§bCloud §7»§7§m-------------");
                team.sendMessage("§cTyp » §4KICK");
                team.sendMessage("§7Spieler » §c " + target.getName());
                team.sendMessage("§7Von » §c" + p.getName());
                team.sendMessage("§7§m-------------§bCloud §7»§7§m-------------");
            }
        }
    }
}


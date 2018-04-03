package de.fileinputstream.mytraz.bungee.commands;


import de.fileinputstream.mytraz.bungee.manager.BanManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;


public class Banlist
        extends Command {
    public Banlist() {
        super("banlist");
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("server.banlist")) {
            if (BanManager.getBannedPlayers().size() == 0) {

                sender.sendMessage("&§bCloud §7» §cEs gibt noch keine gebannten Spieler!");
                return;
            }

            sender.sendMessage("§cgebannte Spieler:");
            for (String x : BanManager.getBannedPlayers()) {
                sender.sendMessage("§bCloud §7» §6" + x + " §8/check " + x);
            }
        }
    }
}



package de.fileinputstream.mytraz.bungee.commands;


import de.fileinputstream.mytraz.bungee.manager.MuteManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;


public class Mutelist
        extends Command {
    public Mutelist() {
        /* 14 */
        super("mutelist");
    }


    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("server.mute")) {
            if (MuteManager.getMutedPlayers().size() == 0) {
                sender.sendMessage("§bFlippiGames §7» §cEs gibt keine gemuteten Spieler.");
                return;
            }

            sender.sendMessage("§c: ");

            for (String x : MuteManager.getMutedPlayers()) {
                sender.sendMessage("§bFlippiGames §7» §6" + x + " §8/check " + x);
            }
        }
    }
}


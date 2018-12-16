package de.fileinputstream.mytraz.bungee.commands;

import de.fileinputstream.mytraz.bungee.manager.BanManager;
import de.fileinputstream.mytraz.bungee.manager.MuteManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;


public class Check
        extends Command {
    public Check() {
        super("check");
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("server.check")) {
            if (args.length == 0) {
                sender.sendMessage("§bFlippiGames §7» §7/check <Spieler>");
            } else {
                sender.sendMessage("§bFlippiGames §7» §6Spieler Informationen");

                sender.sendMessage("§bFlippiGames §7» §5Informationen werden geladen...");
                sender.sendMessage("§bFlippiGames §7» §eSpieler: §c" + args[0]);
                if (BanManager.isBanned(args[0])) {
                    sender.sendMessage("§bFlippiGames §7» §eSTATUS: §cGebannt");
                    sender.sendMessage("");
                    sender.sendMessage("§bFlippiGames §7» §eVon: §7" +
                            BanManager.getWhoBanned(args[0]));
                    sender.sendMessage("§bFlippiGames §7» §eGrund: §c" +
                            BanManager.getReason(args[0]));
                    sender.sendMessage("§bFlippiGames §7» Verbleibende Zeit : §c" +
                            BanManager.getRemainingTime(args[0]));
                } else {
                    sender.sendMessage("");
                    sender.sendMessage("§bFlippiGames §7» §eSTATUS: §aNicht gebannt.");
                    sender.sendMessage("");
                }
                if (MuteManager.isMuted(args[0])) {
                    sender.sendMessage("");
                    sender.sendMessage("§bFlippiGames §7» §eSTATUS: §cGemutet ");
                    sender.sendMessage("");
                    sender.sendMessage("§bFlippiGames §7» §eVon: §c" +
                            MuteManager.getWhoMuted(args[0]));
                    sender.sendMessage("§bFlippiGames §7» §eGrund: §c" +
                            MuteManager.getReason(args[0]));
                    sender.sendMessage("§bFlippiGames §7» §eVerbleibende Zeit: §7" +
                            MuteManager.getRemainingTime(args[0]));
                } else {
                    sender.sendMessage("§bFlippiGames §7» §eSTATUS: §aNicht gemutet");
                    sender.sendMessage("");
                }
            }
        }
    }
}



package de.fileinputstream.mytraz.bungee.commands;

import de.fileinputstream.mytraz.bungee.manager.MuteManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;


public class Unmute
        extends Command {
    public Unmute() {
        super("unmute");
    }


    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("server.unmute")) {
            if (args.length < 1) {
                sender.sendMessage("§bFlippiGames §7» §cUse /unmute <Spieler> ");
            } else {
                if (!MuteManager.isMuted(args[0])) {
                    sender.sendMessage("§bFlippiGames §7» §cDieser Spieler ist nicht gemutet");
                    return;
                }
                sender.sendMessage("§bFlippiGames §7» Der Spieler wurde gemutet");
                MuteManager.unMute(args[0], sender.getName());
            }
        }
    }
}


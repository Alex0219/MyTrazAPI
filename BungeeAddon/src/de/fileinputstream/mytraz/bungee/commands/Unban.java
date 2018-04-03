package de.fileinputstream.mytraz.bungee.commands;


import de.fileinputstream.mytraz.bungee.manager.BanManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;


public class Unban
        extends Command {
    public Unban() {
        super("unban");
    }


    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("server.unban")) {
            if (args.length != 1) {
                sender.sendMessage("§bCloud §7» §cVerwende /unban <Spieler> ");
            } else {
                if (!BanManager.isBanned(args[0])) {
                    sender.sendMessage("§bCloud §7» §cDieser Spieler ist nicht gebannt.");
                    return;
                }
                sender.sendMessage("§bCloud §7» §cDer Spieler wurde entbannt.");
                BanManager.unBan(args[0], sender.getName());
            }
        }
    }
}



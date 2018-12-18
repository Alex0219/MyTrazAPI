package de.fileinputstream.mytraz.bungee.commands;

import de.fileinputstream.mytraz.bungee.manager.BanManager;
import de.fileinputstream.mytraz.bungee.manager.RankManager;
import de.fileinputstream.mytraz.bungee.manager.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class Ban
        extends Command {
    public Ban() {
        super("ban");
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("server.ban")) {
            if (args.length < 2) {
                sender.sendMessage("§bFlippiGames §7» §cVerwende /ban <Spieler> <Grund> ");
            } else {
                if (BanManager.isBanned(args[0])) {
                    sender.sendMessage("§bFlippiGames §7» §cDieser Spieler ist bereits gebannt.");
                    return;
                }
                String message = "";
                for (int i = 1; i < args.length; i++) {
                    message = message + args[i] + " ";
                }
                ProxiedPlayer player = (ProxiedPlayer) sender;

                System.out.println(sender.getName());


                if (!(sender instanceof ProxiedPlayer)) {
                    BanManager.Ban(args[0], message, sender.getName(), -1);
                    sender.sendMessage("§bFlippiGames §7» §7Der Spieler wurde gebannt.");
                    return;
                } else {

                    String playername = args[0];
                    playername = playername.toLowerCase();

                    String bannedUUID = UUIDFetcher.getUUID(playername);
                    String bannedrank = RankManager.getRank(bannedUUID);

                    String bannerUUID = UUIDFetcher.getUUID(sender.getName());
                    String bannerRank = RankManager.getRank(bannerUUID);
                    if (bannedrank == null || bannedrank.equalsIgnoreCase("")) {
                        BanManager.Ban(args[0], message, sender.getName(), -1);
                        sender.sendMessage("§bFlippiGames §7» §7Der Spieler wurde gebannt.");
                        return;
                    }
                    System.out.println(bannedUUID);
                    System.out.println(bannerUUID);

                    if (!bannerRank.equalsIgnoreCase("admin") && RankManager.getRank(bannedUUID).equalsIgnoreCase("inhaber")) {
                        if (bannerRank.equalsIgnoreCase("partner")) {
                            BanManager.Ban(args[0], message, sender.getName(), -1);
                            sender.sendMessage("§bFlippiGames §7» §7Der Spieler wurde gebannt.");
                            return;
                        }
                        sender.sendMessage("§bFlippiGames §7» §cDu darfst diesen Spieler nicht bannen!");
                        return;
                    } else if (!bannerRank.equalsIgnoreCase("admin") && RankManager.getRank(bannedUUID).equalsIgnoreCase("partner")) {
                        if (bannerRank.equalsIgnoreCase("partner")) {
                            BanManager.Ban(args[0], message, sender.getName(), -1);
                            sender.sendMessage("§bFlippiGames §7» §7Der Spieler wurde gebannt.");
                            return;
                        }
                        sender.sendMessage("§bFlippiGames §7» §cDu darfst diesen Spieler nicht bannen!");
                        return;
                    } else {
                        BanManager.Ban(args[0], message, sender.getName(), -1);
                        sender.sendMessage("§bFlippiGames §7» §7Der Spieler wurde gebannt.");
                        return;
                    }
                }

            }

        }

    }


}







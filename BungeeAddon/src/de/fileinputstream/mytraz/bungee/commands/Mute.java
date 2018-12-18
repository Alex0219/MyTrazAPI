package de.fileinputstream.mytraz.bungee.commands;


import de.fileinputstream.mytraz.bungee.manager.MuteManager;
import de.fileinputstream.mytraz.bungee.manager.RankManager;
import de.fileinputstream.mytraz.bungee.manager.UUIDFetcher;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class Mute
        extends Command {
    public Mute() {
        super("mute");
    }


    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("server.mute")) {
            if (args.length < 2) {
                sender.sendMessage("§bFlippiGames §7» §cVerwende /mute <Spieler> <Grund> ");
            } else {
                if (MuteManager.isMuted(args[0])) {
                    sender.sendMessage("§bFlippiGames §7» §cDieser Spieler ist bereits gemutet.");
                    return;
                }
                String message = "";
                for (int i = 1; i < args.length; i++) {
                    message = message + args[i] + " ";
                }
                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);

                ProxiedPlayer player = (ProxiedPlayer) sender;


                String playername = args[0];
                playername = playername.toLowerCase();

                String bannedUUID = UUIDFetcher.getUUID(playername);
                String bannedrank = RankManager.getRank(bannedUUID);

                String bannerUUID = UUIDFetcher.getUUID(sender.getName());
                String bannerRank = RankManager.getRank(bannerUUID);
                if (bannedrank == null || bannedrank.equalsIgnoreCase("")) {
                    MuteManager.Mute(args[0], message, target.getName(), -1);
                    sender.sendMessage("§bFlippiGames §7» §7Der Spieler wurde gemutet.");
                    return;
                }
                System.out.println(bannedUUID);
                System.out.println(bannerUUID);

                if (!bannerRank.equalsIgnoreCase("admin") && RankManager.getRank(bannedUUID).equalsIgnoreCase("inhaber")) {
                    sender.sendMessage("§bFlippiGames §7» §cDu darfst diesen Spieler nicht muten!");
                    return;
                } else if (!bannerRank.equalsIgnoreCase("admin") && RankManager.getRank(bannedUUID).equalsIgnoreCase("partner")) {
                    sender.sendMessage("§bFlippiGames §7» §cDu darfst diesen Spieler nicht muten!");
                    return;
                } else {
                    MuteManager.Mute(args[0], message, target.getName(), -1);
                    sender.sendMessage("§bFlippiGames §7» §7Der Spieler wurde gemutet.");
                    return;
                }


            }


        }
    }


}



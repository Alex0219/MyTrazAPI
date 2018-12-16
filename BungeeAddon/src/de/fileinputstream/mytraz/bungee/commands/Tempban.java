package de.fileinputstream.mytraz.bungee.commands;


import de.fileinputstream.mytraz.bungee.manager.BanManager;
import de.fileinputstream.mytraz.bungee.manager.RankManager;
import de.fileinputstream.mytraz.bungee.manager.UUIDFetcher;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class Tempban
        extends Command {

    public Tempban() {

        super("tempban");

    }


    public void execute(CommandSender sender, String[] args) {

        if (sender.hasPermission("server.tempban")) {

            if (args.length < 4) {

                sender.sendMessage("§bFlippiGames §7» §cVerwende /tempban <Spieler> <Zeit> <Zeitform> <Grund> ");

            } else {

                if (BanManager.isBanned(args[0])) {
                    sender.sendMessage("§bFlippiGames §7» §cDieser Spieler bereits schon gebannt.");
                    return;
                }

                String TimeUnit = args[2];


                ProxiedPlayer target = BungeeCord.getInstance().getPlayer(args[0]);


                String message = "";

                for (int i = 3; i < args.length; i++) {

                    message = message + args[i] + " ";

                }
                ProxiedPlayer player = (ProxiedPlayer) sender;


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

                if (!bannerRank.equalsIgnoreCase("admin") && RankManager.getRank(bannedUUID).equalsIgnoreCase("admin")) {
                    sender.sendMessage("§bFlippiGames §7» §cDu darfst diesen Spieler nicht bannen!");
                    return;
                } else {
                    int Time = Integer.parseInt(args[1]);
                    if ((TimeUnit.equalsIgnoreCase("sec")) ||
                            (TimeUnit.equalsIgnoreCase("s")) ||
                            (TimeUnit.equalsIgnoreCase("sek")) ||
                            (TimeUnit.equalsIgnoreCase("sekunden")) ||
                            (TimeUnit.equalsIgnoreCase("secs"))) {

                        sender.sendMessage("§bFlippiGames §7» §cDer Spieler wurde gebannt.");

                        BanManager.Ban(args[0], message, sender.getName(), Time * 1);

                    } else if ((TimeUnit.equalsIgnoreCase("min")) ||
                            (TimeUnit.equalsIgnoreCase("minuten")) ||
                            (TimeUnit.equalsIgnoreCase("m")) ||
                            (TimeUnit.equalsIgnoreCase("mins")) ||
                            (TimeUnit.equalsIgnoreCase("minute"))) {


                        sender.sendMessage("§bFlippiGames §7» §cDer Spieler wurde gebannt.");

                        BanManager.Ban(args[0], message, sender.getName(),
                                Time * 60);

                    } else if ((TimeUnit.equalsIgnoreCase("h")) ||
                            (TimeUnit.equalsIgnoreCase("hour")) ||
                            (TimeUnit.equalsIgnoreCase("hours"))) {

                        sender.sendMessage("§bFlippiGames §7» §cDer Spieler wurde gebannt.");

                        BanManager.Ban(args[0], message, sender.getName(),
                                Time * 60 * 60);

                    } else if ((TimeUnit.equalsIgnoreCase("d")) ||
                            (TimeUnit.equalsIgnoreCase("day")) ||
                            (TimeUnit.equalsIgnoreCase("t")) ||
                            (TimeUnit.equalsIgnoreCase("tage")) ||
                            (TimeUnit.equalsIgnoreCase("tag")) ||
                            (TimeUnit.equalsIgnoreCase("days"))) {


                        sender.sendMessage("§bFlippiGames §7» §cDer Spieler wurde gebannt.");

                        BanManager.Ban(args[0], message,
                                sender.getName(), Time * 60 * 60 * 24);

                    } else if ((TimeUnit.equalsIgnoreCase("w")) ||
                            (TimeUnit.equalsIgnoreCase("week")) ||
                            (TimeUnit.equalsIgnoreCase("woche")) ||
                            (TimeUnit.equalsIgnoreCase("wochen")) ||
                            (TimeUnit.equalsIgnoreCase("weeks"))) {

                        sender.sendMessage("§bFlippiGames §7» §cDer Spieler wurde gebannt.");

                        BanManager.Ban(args[0], message,
                                sender.getName(), Time * 60 * 60 *
                                        24 * 7);

                    } else {

                        sender.sendMessage("§bFlippiGames §7» §cBitte verwende diese Zeitformen:  s | m | h | d | w");

                    }
                }

            }


        }

    }

}





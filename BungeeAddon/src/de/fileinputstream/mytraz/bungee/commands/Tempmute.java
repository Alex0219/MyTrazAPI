package de.fileinputstream.mytraz.bungee.commands;


import de.fileinputstream.mytraz.bungee.manager.MuteManager;
import de.fileinputstream.mytraz.bungee.manager.RankManager;
import de.fileinputstream.mytraz.bungee.manager.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class Tempmute

        extends Command {
    public Tempmute() {
        super("tempmute");
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("server.tempmute")) {
            if (args.length < 4) {
                sender.sendMessage("§bCloud §7» §cVerwende /tempmute <Spieler> <Zeit> <Zeitform> <Grund> ");
            } else {
                if (MuteManager.isMuted(args[0])) {
                    sender.sendMessage("§bCloud §7»§cDieser Spieler ist bereits gemutet.");
                    return;
                }

                ProxiedPlayer player = (ProxiedPlayer) sender;


                String playername = args[0];
                playername = playername.toLowerCase();

                String bannedUUID = UUIDFetcher.getUUID(playername);
                String bannedrank = RankManager.getRank(bannedUUID);

                String bannerUUID = UUIDFetcher.getUUID(sender.getName());
                String bannerRank = RankManager.getRank(bannerUUID);
                if (bannedrank == null || bannedrank.equalsIgnoreCase("")) {
                    String TimeUnit = args[2];
                    String message = "";
                    for (int i = 3; i < args.length; i++) {
                        message = message + args[i] + " ";
                    }
                    int Time = Integer.parseInt(args[1]);
                    if ((TimeUnit.equalsIgnoreCase("sec")) ||
                            (TimeUnit.equalsIgnoreCase("s")) ||
                            (TimeUnit.equalsIgnoreCase("sek")) ||
                            (TimeUnit.equalsIgnoreCase("sekunden")) ||
                            (TimeUnit.equalsIgnoreCase("secs"))) {
                        sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                        MuteManager.Mute(args[0], message, sender.getName(),
                                Time * 1);
                    } else if ((TimeUnit.equalsIgnoreCase("min")) ||
                            (TimeUnit.equalsIgnoreCase("minute")) ||
                            (TimeUnit.equalsIgnoreCase("m")) ||
                            (TimeUnit.equalsIgnoreCase("mins")) ||
                            (TimeUnit.equalsIgnoreCase("minutes"))) {


                        sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                        MuteManager.Mute(args[0], message, sender.getName(),
                                Time * 60);
                    } else if ((TimeUnit.equalsIgnoreCase("h")) ||
                            (TimeUnit.equalsIgnoreCase("hour")) ||
                            (TimeUnit.equalsIgnoreCase("hours"))) {

                        sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                        MuteManager.Mute(args[0], message,
                                sender.getName(), Time * 60 * 60);
                    } else if ((TimeUnit.equalsIgnoreCase("d")) ||
                            (TimeUnit.equalsIgnoreCase("t")) ||
                            (TimeUnit.equalsIgnoreCase("tage")) ||
                            (TimeUnit.equalsIgnoreCase("tag")) ||
                            (TimeUnit.equalsIgnoreCase("day")) ||
                            (TimeUnit.equalsIgnoreCase("days"))) {

                        sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                        MuteManager.Mute(args[0], message,
                                sender.getName(), Time * 60 * 60 * 24);
                    } else if ((TimeUnit.equalsIgnoreCase("w")) ||
                            (TimeUnit.equalsIgnoreCase("week")) ||
                            (TimeUnit.equalsIgnoreCase("woche")) ||
                            (TimeUnit.equalsIgnoreCase("wochen")) ||
                            (TimeUnit.equalsIgnoreCase("weeks"))) {
                        sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                        MuteManager.Mute(args[0], message,
                                sender.getName(), Time * 60 * 60 *
                                        24 * 7);

                    } else {
                        sender.sendMessage("§bCloud §7» §cBitte verwende diese Zeitformen:  s | m | h | d | w");
                    }
                }
                System.out.println(bannedUUID);
                System.out.println(bannerUUID);

                if (!bannerRank.equalsIgnoreCase("admin") && RankManager.getRank(bannedUUID).equalsIgnoreCase("admin")) {
                    sender.sendMessage("§bCloud §7» §cDu darfst diesen Spieler nicht muten!");
                    return;
                } else {
                    String TimeUnit = args[2];
                    String message = "";
                    for (int i = 3; i < args.length; i++) {
                        message = message + args[i] + " ";
                    }
                    int Time = Integer.parseInt(args[1]);
                    if ((TimeUnit.equalsIgnoreCase("sec")) ||
                            (TimeUnit.equalsIgnoreCase("s")) ||
                            (TimeUnit.equalsIgnoreCase("sek")) ||
                            (TimeUnit.equalsIgnoreCase("sekunden")) ||
                            (TimeUnit.equalsIgnoreCase("secs"))) {
                        sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                        MuteManager.Mute(args[0], message, sender.getName(),
                                Time * 1);
                    } else if ((TimeUnit.equalsIgnoreCase("min")) ||
                            (TimeUnit.equalsIgnoreCase("minute")) ||
                            (TimeUnit.equalsIgnoreCase("m")) ||
                            (TimeUnit.equalsIgnoreCase("mins")) ||
                            (TimeUnit.equalsIgnoreCase("minutes"))) {


                        sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                        MuteManager.Mute(args[0], message, sender.getName(),
                                Time * 60);
                    } else if ((TimeUnit.equalsIgnoreCase("h")) ||
                            (TimeUnit.equalsIgnoreCase("hour")) ||
                            (TimeUnit.equalsIgnoreCase("hours"))) {

                        sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                        MuteManager.Mute(args[0], message,
                                sender.getName(), Time * 60 * 60);
                    } else if ((TimeUnit.equalsIgnoreCase("d")) ||
                            (TimeUnit.equalsIgnoreCase("t")) ||
                            (TimeUnit.equalsIgnoreCase("tage")) ||
                            (TimeUnit.equalsIgnoreCase("tag")) ||
                            (TimeUnit.equalsIgnoreCase("day")) ||
                            (TimeUnit.equalsIgnoreCase("days"))) {

                        sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                        MuteManager.Mute(args[0], message,
                                sender.getName(), Time * 60 * 60 * 24);
                    } else if ((TimeUnit.equalsIgnoreCase("w")) ||
                            (TimeUnit.equalsIgnoreCase("week")) ||
                            (TimeUnit.equalsIgnoreCase("woche")) ||
                            (TimeUnit.equalsIgnoreCase("wochen")) ||
                            (TimeUnit.equalsIgnoreCase("weeks"))) {
                        sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                        MuteManager.Mute(args[0], message,
                                sender.getName(), Time * 60 * 60 *
                                        24 * 7);

                    } else {
                        sender.sendMessage("§bCloud §7» §cBitte verwende diese Zeitformen:  s | m | h | d | w");
                    }
                }

                String TimeUnit = args[2];
                String message = "";
                for (int i = 3; i < args.length; i++) {
                    message = message + args[i] + " ";
                }
                int Time = Integer.parseInt(args[1]);
                if ((TimeUnit.equalsIgnoreCase("sec")) ||
                        (TimeUnit.equalsIgnoreCase("s")) ||
                        (TimeUnit.equalsIgnoreCase("sek")) ||
                        (TimeUnit.equalsIgnoreCase("sekunden")) ||
                        (TimeUnit.equalsIgnoreCase("secs"))) {
                    sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                    MuteManager.Mute(args[0], message, sender.getName(),
                            Time * 1);
                } else if ((TimeUnit.equalsIgnoreCase("min")) ||
                        (TimeUnit.equalsIgnoreCase("minute")) ||
                        (TimeUnit.equalsIgnoreCase("m")) ||
                        (TimeUnit.equalsIgnoreCase("mins")) ||
                        (TimeUnit.equalsIgnoreCase("minutes"))) {


                    sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                    MuteManager.Mute(args[0], message, sender.getName(),
                            Time * 60);
                } else if ((TimeUnit.equalsIgnoreCase("h")) ||
                        (TimeUnit.equalsIgnoreCase("hour")) ||
                        (TimeUnit.equalsIgnoreCase("hours"))) {

                    sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                    MuteManager.Mute(args[0], message,
                            sender.getName(), Time * 60 * 60);
                } else if ((TimeUnit.equalsIgnoreCase("d")) ||
                        (TimeUnit.equalsIgnoreCase("t")) ||
                        (TimeUnit.equalsIgnoreCase("tage")) ||
                        (TimeUnit.equalsIgnoreCase("tag")) ||
                        (TimeUnit.equalsIgnoreCase("day")) ||
                        (TimeUnit.equalsIgnoreCase("days"))) {

                    sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                    MuteManager.Mute(args[0], message,
                            sender.getName(), Time * 60 * 60 * 24);
                } else if ((TimeUnit.equalsIgnoreCase("w")) ||
                        (TimeUnit.equalsIgnoreCase("week")) ||
                        (TimeUnit.equalsIgnoreCase("woche")) ||
                        (TimeUnit.equalsIgnoreCase("wochen")) ||
                        (TimeUnit.equalsIgnoreCase("weeks"))) {
                    sender.sendMessage("§bCloud §7» §7Der Spieler wurde gemutet.");

                    MuteManager.Mute(args[0], message,
                            sender.getName(), Time * 60 * 60 *
                                    24 * 7);

                } else {
                    sender.sendMessage("§bCloud §7» §cBitte verwende diese Zeitformen:  s | m | h | d | w");
                }
            }

        }


    }

}









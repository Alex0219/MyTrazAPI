package de.fileinputstream.none.api.commands;

import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.rank.RankManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class CommandRang implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String uuid = UUIDFetcher.getUUID(p.getName()).toString();
            RankManager.getRank(uuid, new Consumer<String>() {
                @Override
                public void accept(String s) {
                    if (s.equalsIgnoreCase("Admin".toLowerCase()) || p.isOp()) {
                        if (args.length == 2) {
                            if (args[1].equalsIgnoreCase("Admin".toLowerCase()) || args[1].equalsIgnoreCase("Dev".toLowerCase()) || args[1].equalsIgnoreCase("Mod".toLowerCase()) || args[1].equalsIgnoreCase("Sup".toLowerCase()) || args[1].equalsIgnoreCase("Architekt".toLowerCase()) || args[1].equalsIgnoreCase("YouTuber") || args[1].equalsIgnoreCase("Premium".toLowerCase()) || args[1].equalsIgnoreCase("Spieler".toLowerCase()) || args[1].equalsIgnoreCase("Bauleitung".toLowerCase()) || args[1].equalsIgnoreCase("Premiumplus".toLowerCase()) || args[1].equalsIgnoreCase("Teamleitung".toLowerCase())) {
                                String rank = args[1];
                                RankManager.setRank(UUIDFetcher.getUUID(args[0]).toString(), rank);
                                p.sendMessage("§7------------------------------------------------------");
                                p.sendMessage("§cSystem §7● §aDer Rang wurde zu §c" + args[1] + " gesetzt.");
                                p.sendMessage("§7------------------------------------------------------");


                            } else {
                                p.sendMessage("§cSystem §7● §cDieser Rang existiert nicht.");

                            }
                        } else {
                            p.sendMessage("§cSystem §7● Verwende §7/rank <Spieler> <Rank>");
                        }
                    }
                }
            });

            return false;
        }
        return false;
    }
}

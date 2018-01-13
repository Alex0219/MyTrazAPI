package de.fileinputstream.none.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.rank.RankManager;

public class CommandRang implements CommandExecutor {

	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String uuid = UUIDFetcher.getUUID(p.getName()).toString();
            if (RankManager.getRank(uuid).equalsIgnoreCase("Admin".toLowerCase())) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("Admin".toLowerCase()) || args[1].equalsIgnoreCase("Dev".toLowerCase()) || args[1].equalsIgnoreCase("Mod".toLowerCase()) || args[1].equalsIgnoreCase("Sup".toLowerCase()) || args[1].equalsIgnoreCase("Architekt".toLowerCase()) || args[1].equalsIgnoreCase("YouTuber") || args[1].equalsIgnoreCase("Premium".toLowerCase()) || args[1].equalsIgnoreCase("Spieler".toLowerCase()) || args[1].equalsIgnoreCase("Bauleitung".toLowerCase()) || args[1].equalsIgnoreCase("Premiumplus".toLowerCase()) || args[1].equalsIgnoreCase("Teamleitung".toLowerCase())) {
                        String rank = args[1];
                        RankManager.setRank(UUIDFetcher.getUUID(args[0]).toString(), rank);
                        p.sendMessage("§7------------------------------------------------------");
                        p.sendMessage("§cSystem §7● §aDer Rang wurde zu §c" + args[1] + " gesetzt.");
                        p.sendMessage("§7------------------------------------------------------");
                    
                        if (Bukkit.getPlayer(args[0]) != null) {
                            Bukkit.getPlayer(args[0]).kickPlayer("§cSystem §7● Dein Rang wurde zu §c" + args[1].toUpperCase() + " §7gesetzt.");
                        }
                    } else {
                        p.sendMessage("§cSystem §7● §cDieser Rang existiert nicht.");
                        return true;
                    }
                } else {
                    p.sendMessage("§cSystem §7● Verwende §7/rank <Spieler> <Rank>");
                }
            } else {
            }
            return false;
        }
        return false;
    }
}

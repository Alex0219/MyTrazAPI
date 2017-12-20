package de.fileinputstream.none.api.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.punishment.HistoryManager;

public class CommandHistory implements CommandExecutor {
	
	 @Override
	    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	        if ((sender instanceof Player)) {
	            Player p = (Player) sender;
	            if (p.hasPermission("ban.history")) {
	                if (args.length == 1) {
	                    String playername = args[0].toLowerCase();
	                    String uuid = UUIDFetcher.getUUID(playername).toString();
	                    if (HistoryManager.isRegistered(uuid)) {
	                        p.sendMessage("§cSystem §7● §eHistory von §8" + playername);
	                        List < String > history = HistoryManager.getEntrys(uuid);
	                        for (int i = 0; i < history.size(); i++) {
	                            String[] historyEntry = ((String) history.get(i)).split("#Cut#");
	                            p.sendMessage("§cSystem §7● §3Typ: §c" + historyEntry[0] + " §3Erstellt: §a" + historyEntry[4] + " §3Grund: §c" + historyEntry[2] + " §3Von: §e" + historyEntry[1] + " §3Zeit: §a" + historyEntry[3]);
	                        }
	                    } else {
	                        p.sendMessage("§cSystem §7● §cHistory ist leer.");
	                    }
	                } else if (args.length == 2) {
	                    if ((args[0].equalsIgnoreCase("clear")) && (p.hasPermission("ban.history"))) {
	                        String playername = args[1].toLowerCase();
	                        String uuid = UUIDFetcher.getUUID(playername).toString();
	                        if (HistoryManager.isRegistered(uuid)) {
	                            HistoryManager.delHistory(uuid);
	                            p.sendMessage("§cSystem §7● §aHistory wurde geleert.");
	                        } else {
	                            p.sendMessage("§cSystem §7● §cHistory ist leer.");
	                        }
	                    }
	                } else {
	                    p.sendMessage("§cSystem §7● §c/history <Player>");
	                }
	            } else {
	                p.sendMessage("§cSystem §7● §cYou do not have permission to execute this command!");
	            }
	        } else if ((sender instanceof ConsoleCommandSender)) {
	            if (args.length == 1) {
	                String playername2 = args[0].toLowerCase();
	                String Player2 = Bukkit.getOfflinePlayer(playername2).getName();
	                String uuid = UUIDFetcher.getUUID(playername2).toString();
	                if (HistoryManager.isRegistered(uuid)) {
	                    sender.sendMessage("§cSystem §7● §eHistory von §8" + Player2);
	                    List < String > history2 = HistoryManager.getEntrys(uuid);
	                    for (int j = 0; j < history2.size(); j++) {
	                        String[] historyEntry2 = ((String) history2.get(j)).split("#Cut#");
	                        sender.sendMessage("§cSystem §7● §3Typ: §c" + historyEntry2[0] + " §3Erstellt: §a" + historyEntry2[4] + " §3Grund: §c" + historyEntry2[2] + " §3Von: §e" + historyEntry2[1] + " §3Zeit: §a" + historyEntry2[3]);
	                    }
	                } else {
	                    sender.sendMessage("§cSystem §7● §cHistory ist leer");
	                }
	            } else if ((args.length == 2) && (args[0].equalsIgnoreCase("clear"))) {
	                String playername2 = args[1].toLowerCase();
	                String uuid = UUIDFetcher.getUUID(playername2).toString();
	                if (HistoryManager.isRegistered(uuid)) {
	                    HistoryManager.delHistory(uuid);
	                    sender.sendMessage("§cSystem §7● §aDie History wurde geleert.");
	                }
	            }
	        }
	        return true;
	    }

}

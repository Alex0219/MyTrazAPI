package de.fileinputstream.none.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.blogspot.debukkitsblog.net.Datapackage;

import de.fileinputstream.none.api.Bootstrap;
import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.punishment.BanManager;

public class CommandUnBan implements CommandExecutor {
	
	 public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	        if ((sender instanceof Player)) {
	            Player p = (Player) sender;
	            if (p.hasPermission("api.ban.unban")) {
	                if (args.length == 1) {
	                    String playername = args[0];
	                    playername = playername.toLowerCase();
	                    String uuid = UUIDFetcher.getUUID(playername).toString();
	                 
	                    if (Bootstrap.getMysql().isConnected()) {
	                        if (!BanManager.isBanned(getUUID(playername))) {
	                            StringBuilder sb = new StringBuilder();

	                            sender.sendMessage("§cSystem §7● §cDieser Spieler ist derzeit nicht gebannt§8!");
	                            return true;
	                        }
	                        BanManager.unban(getUUID(playername));
	                        StringBuilder sb2 = new StringBuilder();

	                        sender.sendMessage(" §cSystem §7● §7Du hast den Spieler " + playername + " §7entbannt!");
	                        return true;
	                    }
	                    p.sendMessage("§cSystem §7● §4[ERROR] §cEs besteht keine Datenbankverbindung!");
	                } else {
	                    p.sendMessage("§cSystem §7● §c/unban <Spieler>");
	                }
	            } else {
	                p.sendMessage("§cSystem §7● §cYou do not have permission to execute this command!");
	            }
	        } else if (((sender instanceof ConsoleCommandSender)) && (args.length == 1)) {
	            String playername2 = args[0];
	            playername2 = playername2.toLowerCase();
	         
	            if (Bootstrap.getMysql().isConnected()) {
	                if (!BanManager.isBanned(getUUID(playername2))) {
	                    sender.sendMessage("§cSystem §7● §7Dieser Spieler ist derzeit nicht gebannt§8!");
	                    return true;
	                }
	                BanManager.unban(getUUID(playername2));
	                StringBuilder sb4 = new StringBuilder();

	                sender.sendMessage("§cSystem §7● §7Der Spieler wurde entbannt.");

	                return true;
	            }
	            sender.sendMessage("§cSystem §7● §4[ERROR] §cEs besteht keine Datenbankverbindung!");
	        }
	        return true;
	    }

	    private String getUUID(String Playername) {
	        return UUIDFetcher.getUUID(Playername).toString();
	    }
}

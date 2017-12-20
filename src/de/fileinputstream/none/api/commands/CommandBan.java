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
import de.fileinputstream.none.api.punishment.HistoryManager;

public class CommandBan implements CommandExecutor  {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            if (p.hasPermission("ban.ban")) {
                if (args.length >= 2) {
                    String playername = args[0];
                    if (Bootstrap.getMysql().isConnected()) {
                        playername = playername.toLowerCase();

                        String uuid = UUIDFetcher.getUUID(playername).toString();
                        String Player = Bukkit.getOfflinePlayer(playername).getName();
                        if (BanManager.isBanned(uuid)) {
                            sender.sendMessage("§cSystem §7● §7Dieser Spieler ist bereits §cgebannt");
                            return true;
                        }
                        String reason = "";
                        for (int i = 1; i < args.length; i++) {
                            reason = reason + args[i] + " ";
                        }
           /*
            *              if (PermissionsEx.getUser(Player).has("ban.noban") && ((p.hasPermission("ban.banall")))) {
            *                       sender.sendMessage("§cSystem §7● §7Du darfst kein §cTeammitglied §7bannen!");
                            return true;
                        }
            */
                   
                        BanManager.ban(uuid, Player, reason, -1L, "p", p.getName(), BanManager.createbanID("abcdefghijklmnopqrstuvwxyz", 12));
                        HistoryManager.addHistoryEntry("Ban", uuid, reason, p.getName(), "permanent", Player);
                      
                        sender.sendMessage(" §cSystem §7● §7Du hast den Spieler §c" + Player + " §7gebannt!");
                        return true;
                    }
                    p.sendMessage("§cSystem §7● §4[ERROR] §cEs besteht keine Datenbankverbindung");
                }
                sender.sendMessage("§cSystem §7● §c/ban <Spieler> <Grund>");
                return true;
            }
            p.sendMessage("§cSystem §7● §cYou do not have permission to execute this command!");

            return true;
        }
        if ((sender instanceof ConsoleCommandSender)) {
            if (args.length >= 2) {
                String playername2 = args[0];
                if (Bootstrap.getMysql().isConnected()) {
                    playername2 = playername2.toLowerCase();
                    String uuid = UUIDFetcher.getUUID(playername2).toString();

                    if (BanManager.isBanned(uuid)) {
                        sender.sendMessage("§cSystem §7● §7Dieser Spieler ist bereits §cgebannt");
                        return true;
                    }
                    String reason2 = "";
                    for (int j = 1; j < args.length; j++) {
                        reason2 = reason2 + args[j] + " ";
                    }
                    BanManager.ban(uuid, playername2, reason2, -1L, "p", "CONSOLE", BanManager.createbanID("abcdefghijklmnopqrstuvwxyz", 7));
                    HistoryManager.addHistoryEntry("Ban", uuid, reason2, "CONSOLE", "permanent", playername2);
                    Bootstrap.getResilentClient().sendMessage(new Datapackage("KickBanned", playername2));
                    sender.sendMessage(" §cSystem §7● §7Du hast den Spieler §c" + playername2 + " §7gebannt!");
                    
                    return true;
                }
                if (Bukkit.getOfflinePlayer(playername2).isBanned()) {
                    Bukkit.getOfflinePlayer(playername2).setBanned(false);
                    sender.sendMessage(" §cSystem §7● §7Du hast den Spieler " + playername2 + " §7gebannt!");
                } else {
                    sender.sendMessage("§cSystem §7● §7Dieser Spieler ist nicht §cgebannt.");
                }
            }
            sender.sendMessage("§cSystem §7● §c/ban <Spieler> <Grund>");
            return true;
        }
        return true;
    }

}

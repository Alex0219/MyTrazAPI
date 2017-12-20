package de.fileinputstream.none.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.Main;
import org.bukkit.entity.Player;

import de.fileinputstream.none.api.Bootstrap;
import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.punishment.HistoryManager;
import de.fileinputstream.none.api.punishment.MuteManager;

public class CommandMute implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            if (p.hasPermission("ban.mute")) {
                if (args.length >= 2) {
                    String playername = args[0];
                    if (Bootstrap.getMysql().isConnected()) {
                        playername = playername.toLowerCase();
                        String uuid = UUIDFetcher.getUUID(playername).toString();
                        if (MuteManager.isMuted(uuid)) {
                            sender.sendMessage("§cSystem §7● §cDieser Spieler ist bereits gemutet");
                            return true;
                        }
                        String reason = "";
                        for (int i = 1; i < args.length; i++) {
                            reason = reason + args[i] + " ";
                        }
                   /*
                    *      if (PermissionsEx.getUser(Player).has("ban.noban") && ((!sender.hasPermission("ban.banall")))) {
                    *         sender.sendMessage("§cSystem §7● §7Du darfst kein §cTeammitglied §7muten!");
                            return true;
                        }
                    */
                         
                        MuteManager.mute(uuid, playername, reason, -1L, "p", p.getName());
                        HistoryManager.addHistoryEntry("Mute", uuid, reason, p.getName(), "permanent", playername);

                        sender.sendMessage("§cSystem §7● §7Du hast den Spieler §c" + playername + " §7gemutet.");
                        return true;
                    }
                    p.sendMessage("§cSystem §7● §4[ERROR] §cEs besteht keine Datenbankverbindung");
                }
                sender.sendMessage("§cSystem §7● §c/mute <Spieler> <Grund>");
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
                    if (MuteManager.isMuted(uuid)) {
                        sender.sendMessage("§cSystem §7● §cDieser Spieler ist bereits gemutet");
                        return true;
                    }
                    String reason2 = "";
                    for (int j = 1; j < args.length; j++) {
                        reason2 = reason2 + args[j] + " ";
                    }
                    MuteManager.mute(uuid, playername2, reason2, -1L, "p", "CONSOLE");
                    HistoryManager.addHistoryEntry("Mute",uuid, reason2, "CONSOLE", "permanent", playername2);

                    sender.sendMessage("§cSystem §7● §7Du hast den Spieler §c" + playername2 + " §7gemutet");
                    return true;
                }
                sender.sendMessage("§cSystem §7● §4[ERROR] §cEs besteht keine Datenbankverbindung");
            }
            sender.sendMessage("§cSystem §7● §c/mute <Spieler> <Grund>");
            return true;
        }
        return true;
    }

}

package de.fileinputstream.none.api.commands;

import de.fileinputstream.none.api.Bootstrap;
import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.punishment.HistoryManager;
import de.fileinputstream.none.api.punishment.MuteManager;
import de.fileinputstream.none.api.time.MuteUnit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandTempMute implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if ((sender instanceof ConsoleCommandSender)) {
                if (args.length >= 4) {
                    String playername = args[0];
                    if (Bootstrap.getMysql().isConnected()) {
                        playername = playername.toLowerCase();
                        String Player = Bukkit.getOfflinePlayer(playername).getName();
                        if (MuteManager.isMuted(UUIDFetcher.getUUID(playername).toString())) {
                            sender.sendMessage("§cDieser Spieler ist bereits gemutet");
                            return true;
                        }
                        try {
                            long value = Integer.valueOf(args[1]).intValue();
                        } catch (NumberFormatException e) {
                            long value;
                            e.printStackTrace();

                            return true;
                        }
                        long value = Integer.valueOf(args[1]).intValue();
                        if (value >= 500L) {
                            StringBuilder sb = new StringBuilder();

                            return true;
                        }
                        String unit = args[2];
                        String reason = "";
                        for (int i = 3; i < args.length; i++) {
                            reason = reason + args[i] + " ";
                        }

                        List<String> unitList = MuteUnit.getUnitsAsString();
                        if (unitList.contains(unit.toLowerCase())) {
                            MuteUnit un = MuteUnit.getUnits(unit);
                            long seconds = value * un.getToSecond();
                            String Dauer = value + " " + un.getName();
                            MuteManager.mute(UUIDFetcher.getUUID(playername).toString(), Player, reason, seconds, Dauer, "CONSOLE");
                            HistoryManager.addHistoryEntry("TempMute", UUIDFetcher.getUUID(playername).toString(), reason, "CONSOLE", Dauer, Player);

                            sender.sendMessage("§cSystem §7● §7Du hast den Spieler §c" + Player + " §7gemutet.");
                            return true;
                        }
                        return true;
                    }
                    sender.sendMessage("§cSystem §7● §4[ERROR] §cEs besteht keine Datenbankverbindung!");
                }
                sender.sendMessage("§cSystem §7● §c/tempmute <Spieler> <Zeit> <Format> <Grund>");
            }
            return true;
        }
        Player p = (Player) sender;
        if (p.hasPermission("api.ban.tempmute")) {
            if (args.length >= 4) {
                String playername2 = args[0];
                if (Bootstrap.getMysql().isConnected()) {
                    playername2 = playername2.toLowerCase();
                    String Player2 = Bukkit.getOfflinePlayer(playername2).getName();
                    if (MuteManager.isMuted(UUIDFetcher.getUUID(playername2).toString())) {
                        sender.sendMessage("§cSystem §7● §7Dieser Spieler ist bereits gemutet.");
                        return true;
                    }
                    try {
                        long value2 = Integer.valueOf(args[1]).intValue();
                    } catch (NumberFormatException e2) {
                        long value2;
                        e2.printStackTrace();

                        return true;
                    }
                    long value2 = Integer.valueOf(args[1]).intValue();
                    if (value2 >= 500L) {
                        Player player = p;
                        StringBuilder sb2 = new StringBuilder();

                        return true;
                    }
                    String unit2 = args[2];
                    String reason2 = "";
                    for (int j = 3; j < args.length; j++) {
                        reason2 = reason2 + args[j] + " ";
                    }
                    List<String> unitList2 = MuteUnit.getUnitsAsString();
                    if (unitList2.contains(unit2.toLowerCase())) {
                        MuteUnit un2 = MuteUnit.getUnits(unit2);
                        long seconds2 = value2 * un2.getToSecond();
                        String Dauer2 = value2 + " " + un2.getName();
                        MuteManager.mute(UUIDFetcher.getUUID(playername2).toString(), playername2, reason2, seconds2, Dauer2, p.getName());
                        HistoryManager.addHistoryEntry("TempMute", UUIDFetcher.getUUID(playername2).toString(), reason2, p.getName(), Dauer2, Player2);

                        sender.sendMessage("§cSystem §7● §7Der Spieler ist gemutet.");
                        return true;
                    }
                    return true;
                }
                p.sendMessage("§cSystem §7● §4[ERROR] §cEs besteht keine Datenbankverbindung!");
            }
            sender.sendMessage("§cSystem §7● §c/tempmute <Spieler> <Zeit> <Format> <Grund>");
            return true;
        }
        p.sendMessage("§cDu hast keine Berechtigung f§r diesen Behfel.");
        return true;
    }

}

package de.fileinputstream.none.api.commands;

import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.punishment.BanManager;
import de.fileinputstream.none.api.punishment.HistoryManager;
import de.fileinputstream.none.api.time.BanUnit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandTempBan implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            if ((sender instanceof ConsoleCommandSender)) {
                if (args.length >= 4) {
                    String playername = args[0];
                    playername = playername.toLowerCase();
                    String uuid = UUIDFetcher.getUUID(playername).toString();
                    if (BanManager.isBanned(uuid)) {
                        sender.sendMessage("§cSystem §7● §cDieser Spieler ist bereits gebannt");
                        return true;
                    }
                    try {
                        long value = Integer.valueOf(args[1]).intValue();
                    } catch (NumberFormatException e) {

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


                    List<String> unitList = BanUnit.getUnitsAsString();
                    if (unitList.contains(unit.toLowerCase())) {
                        BanUnit un = BanUnit.getUnits(unit);
                        long seconds = value * un.getToSecond();
                        String Dauer = value + " " + un.getName();
                        BanManager.ban(uuid, playername, reason, seconds, Dauer, "CONSOLE", BanManager.createbanID("abcdefghijklmnopqrstuvwxyz", 7));
                        HistoryManager.addHistoryEntry("TempBan", uuid, reason, "CONSOLE", Dauer, playername);

                        sender.sendMessage("§cSystem §7● §7Du hast den Spieler §c" + playername + " §7gebannt.");
                        return true;
                    }
                    return true;
                }
                sender.sendMessage("§cSystem §7● §c/tempban <Spieler> <Zeit> <Format> <Grund>");
            }
            return true;
        }
        Player p = (Player) sender;
        if (!p.hasPermission("api.ban.tempban")) {
            p.sendMessage("§cSystem §7● §cYou do not have permission to execute this command!");
            return true;
        }
        if (args.length < 4) {
            sender.sendMessage("§cSystem §7● §c/tempban <Spieler> <Zeit> <Format> <Grund>");
            return true;
        }
        String playername2 = args[0];
        playername2 = playername2.toLowerCase();
        String uuid = UUIDFetcher.getUUID(playername2).toString();
        if (BanManager.isBanned(uuid)) {
            sender.sendMessage("§cSystem §7● §cDieser Spieler ist bereits gebannt");
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

        List<String> unitList2 = BanUnit.getUnitsAsString();
        if (unitList2.contains(unit2.toLowerCase())) {
            BanUnit un2 = BanUnit.getUnits(unit2);
            long seconds2 = value2 * un2.getToSecond();
            String Dauer2 = value2 + " " + un2.getName();
            BanManager.ban(uuid, playername2, reason2, seconds2, Dauer2, p.getName(), BanManager.createbanID("abcdefghijklmnopqrstuvwxyz", 7));
            HistoryManager.addHistoryEntry("TempBan", uuid, reason2, p.getName(), Dauer2, playername2);

            sender.sendMessage("§cSystem §7● §7Du hast den Spieler §c" + playername2 + " §7gebannt!");
            return true;
        }
        return true;
    }
}

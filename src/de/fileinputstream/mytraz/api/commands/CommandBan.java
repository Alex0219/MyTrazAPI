package de.fileinputstream.mytraz.api.commands;


import de.fileinputstream.mytraz.api.Bootstrap;
import de.fileinputstream.mytraz.api.cache.UUIDFetcher;
import de.fileinputstream.redisbuilder.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandBan implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            if (p.hasPermission("ban.ban")) {
                if (args.length >= 2) {
                    String playername = args[0];
                    if (Bootstrap.getInstance().getMysql().isConnected()) {
                        playername = playername.toLowerCase();

                        String uuid = UUIDFetcher.getUUID(playername).toString();
                        String rank = RankManager.getRank(UUIDFetcher.getUUID(p.getName()).toString());
                        if (!rank.equalsIgnoreCase("admin") && RankManager.getRank(uuid).equalsIgnoreCase("admin")) {
                            sender.sendMessage("§cSystem §7● §7Du darfst diesen Spieler nicht bannen");
                            return true;
                        }
                        String Player = Bukkit.getOfflinePlayer(playername).getName();
                        if (Bootstrap.getInstance().getBanManager().isBanned(uuid)) {
                            sender.sendMessage("§cSystem §7● §7Dieser Spieler ist bereits §cgebannt");
                            return true;
                        }
                        String reason = "";
                        for (int i = 1; i < args.length; i++) {
                            reason = reason + args[i] + " ";
                        }

                        Bootstrap.getInstance().getBanManager().ban(uuid, Player, reason, -1L, "p", p.getName(), Bootstrap.getInstance().getBanManager().createbanID("abcdefghijklmnopqrstuvwxyz", 12));
                        Bootstrap.getInstance().getHistoryManager().addHistoryEntry("Ban", uuid, reason, p.getName(), "permanent", Player);

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
                if (Bootstrap.getInstance().getMysql().isConnected()) {
                    playername2 = playername2.toLowerCase();
                    String uuid = UUIDFetcher.getUUID(playername2).toString();

                    if (Bootstrap.getInstance().getBanManager().isBanned(uuid)) {
                        sender.sendMessage("§cSystem §7● §7Dieser Spieler ist bereits §cgebannt");
                        return true;
                    }
                    String reason2 = "";
                    for (int j = 1; j < args.length; j++) {
                        reason2 = reason2 + args[j] + " ";
                    }
                    Bootstrap.getInstance().getBanManager().ban(uuid, playername2, reason2, -1L, "p", "CONSOLE", Bootstrap.getInstance().getBanManager().createbanID("abcdefghijklmnopqrstuvwxyz", 7));
                    Bootstrap.getInstance().getHistoryManager().addHistoryEntry("Ban", uuid, reason2, "CONSOLE", "permanent", playername2);

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

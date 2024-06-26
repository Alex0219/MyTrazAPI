package de.fileinputstream.redisbuilder.commands;

import de.fileinputstream.redisbuilder.rank.RankManager;
import de.fileinputstream.redisbuilder.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandRang implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            String uuid = UUIDFetcher.getUUID(p.getName()).toString();

            String s = RankManager.getRank(uuid);
            if (s.equalsIgnoreCase("Admin".toLowerCase()) || p.isOp()) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("Admin".toLowerCase()) || args[1].equalsIgnoreCase("Dev".toLowerCase()) || args[1].equalsIgnoreCase("Mod".toLowerCase()) || args[1].equalsIgnoreCase("Sup".toLowerCase()) || args[1].equalsIgnoreCase("Architekt".toLowerCase()) || args[1].equalsIgnoreCase("YouTuber") || args[1].equalsIgnoreCase("Premium".toLowerCase()) || args[1].equalsIgnoreCase("Spieler".toLowerCase()) || args[1].equalsIgnoreCase("Bauleitung".toLowerCase()) || args[1].equalsIgnoreCase("Premiumplus".toLowerCase()) || args[1].equalsIgnoreCase("Teamleitung".toLowerCase()) || args[1].equalsIgnoreCase("Inhaber".toLowerCase())) {
                        String rank = args[1];
                        RankManager.setRank(UUIDFetcher.getUUID(args[0]).toString(), rank);
                        System.out.println(UUIDFetcher.getUUID(args[0]).toString());
                        p.sendMessage("§7---------------------------------------------------");
                        p.sendMessage("§cSystem §7● §aDer Rang wurde zu §c" + args[1] + " gesetzt.");
                        p.sendMessage("§7---------------------------------------------------");
                        if (Bukkit.getPlayer(args[0]) != null) {
                            Player victim = Bukkit.getPlayer(args[0]);
                            victim.kickPlayer("§aDein Rang wurde aktualisiert!");
                        }

                    } else {
                        p.sendMessage("§cBackend -> §cDieser Rang existiert nicht.");

                    }
                } else {
                    p.sendMessage("§c>Backend -> §7/rank <Spieler> <Rank>");
                }
            }
        }

        return false;
    }
}


package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandCC implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            String uuid = player.getUniqueId().toString();
            String rank = RankManager.getRank(uuid);
            if (rank.equalsIgnoreCase("admin") || rank.equalsIgnoreCase("mod")) {
                Bukkit.getOnlinePlayers().forEach(players -> {
                    DBUser dbusers = new DBUser(players.getUniqueId().toString(), players.getName());
                    if (dbusers.getRank().getRankLevel() < 2) {
                        for (int i = 0; i < 150; i++) {
                            players.sendMessage("");
                        }
                    }
                    players.sendMessage("§bMC-Survival.de §7» §7Der Chat wurde von §c" + player.getDisplayName() + " §7geleert");
                });
            } else {
                player.sendMessage("§cYou do not have permission to execute this command!");
                return true;
            }
        } else {
            return true;
        }
        return false;
    }
}

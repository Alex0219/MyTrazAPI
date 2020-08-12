package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandFly implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            String uuid = player.getUniqueId().toString();
            String rank = RankManager.getRank(uuid);
            if (rank.equalsIgnoreCase("admin") || rank.equalsIgnoreCase("mod")) {
                if (player.getAllowFlight()) {
                    player.setAllowFlight(false);
                    player.sendMessage("§bMC-Survival.de §7» §7Du kannst nun nicht mehr fliegen.");
                } else {
                    player.setAllowFlight(true);
                    player.sendMessage("§bMC-Survival.de §7» §7Du kannst nun fliegen.");
                }
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

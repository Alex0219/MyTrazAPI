package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 15.12.2018 at 12:44
 */
public class CommandWorldResidents implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String uuid = player.getUniqueId().toString();
            String rank = RankManager.getRank(uuid);
            if (rank.equalsIgnoreCase("admin") || rank.equalsIgnoreCase("sup") || rank.equalsIgnoreCase("mod")) {
                if (args.length == 1) {
                    String playername = args[0];
                    String targetUUID = UUIDFetcher.getUUID(playername).toString();
                    player.sendMessage("§7===========§a" + playername + "§7==============");
                    player.sendMessage("§aWelteninfo:§4 " + playername);
                    player.sendMessage("§aWelten:§6 " + Bootstrap.getInstance().getWorldManager().getWorld(targetUUID));
                    player.sendMessage("§7===========§a" + playername + "§7==============");
                    if (RankManager.getRank(targetUUID).equalsIgnoreCase("")) {
                        player.sendMessage("§bMC-Survival.de §7» Dieser Spieler existiert nicht!");

                    }
                } else {
                    player.sendMessage("§bMC-Survival.de §7» Verwende /worldinfo <Spieler>");
                    return true;
                }
            } else {

            }
        } else {
            sender.sendMessage("§bMC-Survival.de §7» Nur Spieler können diesen Befehö ausführen.");
        }
        return false;
    }
}

package de.fileinputstream.mytraz.worldmanagement.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class CommandVanish implements CommandExecutor {

    private static ArrayList<Player> vanishedPlayers = new ArrayList<Player>();

    public static ArrayList<Player> getVanishedPlayers() {
        return vanishedPlayers;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("server.vanish")) {
                if (args.length == 0) {
                    if (!getVanishedPlayers().contains(player)) {
                        player.sendMessage("§cDu bist nun im Vanish.");
                        getVanishedPlayers().add(player);
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            players.hidePlayer(player);
                        });
                    } else {
                        player.sendMessage("§cDu bist nun nicht mehr im Vanish.");
                        getVanishedPlayers().remove(player);
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            players.showPlayer(player);
                        });
                        return true;
                    }
                } else {
                    player.sendMessage("§cVerwende /v");
                    return true;
                }
            } else {
                player.sendMessage("§cDu hast keine Rechte, um diesen Befehl auszuführen.");
                return true;
            }
        } else {
            return true;
        }

        return false;
    }
}

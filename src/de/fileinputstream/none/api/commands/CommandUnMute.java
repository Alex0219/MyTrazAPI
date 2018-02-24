package de.fileinputstream.none.api.commands;

import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.punishment.MuteManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandUnMute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((sender instanceof Player)) {
            Player p = (Player) sender;
            if (p.hasPermission("api.ban.unmute")) {
                if (args.length == 1) {
                    String playername = args[0];
                    playername = playername.toLowerCase();
                    String Player = Bukkit.getOfflinePlayer(playername).getName();
                    if (!MuteManager.isMuted(getUUID(Player))) {
                        StringBuilder sb = new StringBuilder();

                        sender.sendMessage("§cSystem §7● §cDieser Spieler ist derzeit nicht gemutet.");
                        return true;
                    }
                    MuteManager.unmute(getUUID(Player));

                    sender.sendMessage("§cSystem §7● §7Der Spieler wurde entmutet.");
                    return true;
                }
                p.sendMessage("§cSystem §7● §c/unmute <Spieler>");
                return true;
            }
            p.sendMessage("§cSystem §7● §cYou do not have permission to execute this command!");
        } else if (((sender instanceof ConsoleCommandSender)) && (args.length == 1)) {
            String playername2 = args[0];
            playername2 = playername2.toLowerCase();
            String Player2 = Bukkit.getOfflinePlayer(playername2).getName();
            if (!MuteManager.isMuted(getUUID(Player2))) {
                StringBuilder sb3 = new StringBuilder();

                sender.sendMessage("§cSystem §7● §7Dieser Spieler ist derzeit nicht §cgemutet.");
                return true;
            }
            MuteManager.unmute(getUUID(Player2));
            StringBuilder sb4 = new StringBuilder();

            sender.sendMessage("§cSystem §7● §7Der Spieler wurde entmutet.");
            return true;
        }
        return false;
    }

    private String getUUID(String Playername) {
        return UUIDFetcher.getUUID(Playername).toString();
    }

}

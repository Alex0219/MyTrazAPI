package de.fileinputstream.none.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.punishment.BanManager;
import de.fileinputstream.none.api.punishment.MuteManager;

public class CommandCheck implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if ((sender instanceof Player))
        {
            Player p = (Player)sender;
            if (p.hasPermission("api.ban.check"))
            {
                if (args.length == 1)
                {
                    String playername = args[0];
                    playername = playername.toLowerCase();
                    String uuid = UUIDFetcher.getUUID(playername).toString();
                    sender.sendMessage("§8:==---§b[§cPlayer§6Info§b]§8---==:");
                    if (Bukkit.getPlayer(playername) != null)
                    {
                        sender.sendMessage("§8[§aOnline§8]");
                        sender.sendMessage("§3Gamemode§8: §5" + Bukkit.getPlayer(playername).getGameMode());
                    }
                    else
                    {
                        sender.sendMessage("§8[§cOffline§8]");
                    }
                    if (Bukkit.getPlayer(playername) != null) {
                        sender.sendMessage("§3Name§8: " + Bukkit.getPlayer(playername).getDisplayName());
                    } else {
                        sender.sendMessage("§3Name§8: §a" + Bukkit.getOfflinePlayer(playername).getName());
                    }
                    sender.sendMessage("§3Gebannt§8: " + (BanManager.isBanned(getUUID(playername)) ? "§aJa" : "§cNein"));
                    if (BanManager.isBanned(getUUID(playername)))
                    {
                        sender.sendMessage("§3Grund§8: §c" + BanManager.getReason(getUUID(playername)));
                        sender.sendMessage("§3Verbleibende Zeit§8: §e" + BanManager.getRemainingTime(getUUID(playername)));
                        sender.sendMessage("§3Gebannt von§8: §c" + BanManager.getBanner(getUUID(playername)));
                    }
                    sender.sendMessage("§3Gemutet§8: " + (MuteManager.isMuted(getUUID(playername)) ? "§aJa" : "§cNein"));
                    if (MuteManager.isMuted(getUUID(playername)))
                    {
                        sender.sendMessage("§3Grund§8: §c" + MuteManager.getReason(getUUID(playername)));
                        sender.sendMessage("§3Verbleibende Zeit§8: §e" + MuteManager.getRemainingTime(getUUID(playername)));
                        sender.sendMessage("§3Gemuted von§8: §c" + MuteManager.getMuter(getUUID(playername)));
                    }
                    if (Bukkit.getPlayer(playername) != null)
                    {
                        if (Bukkit.getPlayer(playername).getAllowFlight()) {
                            sender.sendMessage("§3Fly§8: §aJa");
                        } else {
                            sender.sendMessage("§3Fly§8: §cNein");
                        }
                    }
                    else {
                        sender.sendMessage("§3Fly§8: §cNein");
                    }
                    return true;
                }
                sender.sendMessage("§cSystem §7● §c/check <Spieler>");
            }
            return true;
        }
        if ((sender instanceof ConsoleCommandSender))
        {
            if (args.length == 1)
            {
                String playername2 = args[0];
                playername2 = playername2.toLowerCase();
                String Player2 = Bukkit.getOfflinePlayer(playername2).getName();
                sender.sendMessage("§8:==---§b[§cPlayer§6Info§b]§8---==:");
                if (Bukkit.getPlayer(Player2) != null)
                {
                    sender.sendMessage("§aOnline");
                    sender.sendMessage("§3Gamemode§8: §5" + Bukkit.getPlayer(Player2).getGameMode());
                }
                else
                {
                    sender.sendMessage("§cOffline");
                }
                if (Bukkit.getPlayer(Player2) != null) {
                    sender.sendMessage("§3Name§8: " + Bukkit.getPlayer(Player2).getDisplayName());
                } else {
                    sender.sendMessage("§3Name§8: §a" + Bukkit.getOfflinePlayer(Player2).getName());
                }
                sender.sendMessage("§3Gebannt§8: " + (BanManager.isBanned(getUUID(Player2)) ? "§aJa" : "§cNein"));
                if (BanManager.isBanned(getUUID(Player2))) {
                    try
                    {
                        sender.sendMessage("§3Grund§8: §c" + BanManager.getReason(getUUID(Player2)));
                        sender.sendMessage("§3Verbleibende Zeit§8: §e" + BanManager.getRemainingTime(getUUID(Player2)));
                        sender.sendMessage("§3Gebannt von§8: §c" + BanManager.getBanner(Bukkit.getPlayer(Player2).getUniqueId().toString()));
                    }
                    catch (Exception localException) {}
                }
                sender.sendMessage("§3Gemutet§8: " + (MuteManager.isMuted(getUUID(Player2)) ? "§aJa" : "§cNein"));
                if (MuteManager.isMuted(getUUID(Player2)))
                {
                    sender.sendMessage("§3Grund§8: §c" + MuteManager.getReason(getUUID(Player2)));
                    sender.sendMessage("§3Verbleibende Zeit§8: §e" + MuteManager.getRemainingTime(getUUID(Player2)));
                    sender.sendMessage("§3Gemutet von§8: §c" + MuteManager.getMuter(Bukkit.getPlayer(Player2).getUniqueId().toString()));
                }
                return true;
            }
            sender.sendMessage("§cSystem §7● §c/info <Spieler>");
        }
        return true;
    }

    private String getUUID(String Playername)
    {
        return UUIDFetcher.getUUID(Playername).toString();
    }

}

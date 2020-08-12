package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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
            String uuid = player.getUniqueId().toString();
            String rank = RankManager.getRank(uuid);
            if (rank.equalsIgnoreCase("admin") || rank.equalsIgnoreCase("mod")) {
                if (args.length == 0) {
                    if (!getVanishedPlayers().contains(player)) {
                        player.sendMessage("§cDu bist nun im Vanish.");
                        Bukkit.broadcastMessage("§c" + player.getDisplayName() + " §7hat den Server verlassen.");
                        getVanishedPlayers().add(player);
                        int fakePlayerCount = Bukkit.getOnlinePlayers().size() - CommandVanish.getVanishedPlayers().size();
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            players.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Spieler online: " + fakePlayerCount + "/" + Bukkit.getMaxPlayers()));
                        });
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Spieler online: " + fakePlayerCount + "/" + Bukkit.getMaxPlayers()));
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            if (!RankManager.getRank(players.getUniqueId().toString()).equalsIgnoreCase("admin")) {
                                players.hidePlayer(player);
                            }

                        });
                    } else {
                        player.sendMessage("§cDu bist nun nicht mehr im Vanish.");
                        Bukkit.broadcastMessage("§c" + player.getDisplayName() + " §7hat den Server betreten.");
                        getVanishedPlayers().remove(player);
                        int fakePlayerCount = Bukkit.getOnlinePlayers().size() - CommandVanish.getVanishedPlayers().size();
                        Bukkit.getOnlinePlayers().forEach(players -> {
                            players.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Spieler online: " + fakePlayerCount + "/" + Bukkit.getMaxPlayers()));
                        });
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§7Spieler online: " + fakePlayerCount + "/" + Bukkit.getMaxPlayers()));
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
                player.sendMessage("§cYou do not have permission to execute this command!");
                return true;
            }
        } else {
            return true;
        }

        return false;
    }
}

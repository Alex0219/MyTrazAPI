package de.fileinputstream.mytraz.worldmanagement.uuid;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public class NameTags {

    public static void createScoreboardTeam() {
        Bootstrap.getInstance().getMainScoreboard().registerNewTeam("001SuperAdmin").setColor(ChatColor.DARK_RED);
        ;
        Bootstrap.getInstance().getMainScoreboard().registerNewTeam("002Admin").setColor(ChatColor.DARK_RED);
        ;
        Bootstrap.getInstance().getMainScoreboard().registerNewTeam("003Mod").setColor(ChatColor.RED);
        ;
        Bootstrap.getInstance().getMainScoreboard().registerNewTeam("004Stammspieler").setColor(ChatColor.GOLD);
        ;
        Bootstrap.getInstance().getMainScoreboard().registerNewTeam("005Spieler").setColor(org.bukkit.ChatColor.GREEN);

        Bootstrap.getInstance().getMainScoreboard().getTeam("001SuperAdmin").setPrefix("§4Admin §7| §4");
        Bootstrap.getInstance().getMainScoreboard().getTeam("002Admin").setPrefix("§4Admin §7| §4");
        Bootstrap.getInstance().getMainScoreboard().getTeam("003Mod").setPrefix("§cMod §7| §c");
        Bootstrap.getInstance().getMainScoreboard().getTeam("004Stammspieler").setPrefix("§6Stammspieler §7| §6");
        Bootstrap.getInstance().getMainScoreboard().getTeam("005Spieler").setPrefix("§a");

    }

    public static void setTags(final Player player) {
        String userGroup;

        String uuid = player.getUniqueId().toString();
        String s = RankManager.getRank(uuid);
        if (s.equalsIgnoreCase("superadmin".toLowerCase())) {
            userGroup = "001SuperAdmin";
        } else if (s.equalsIgnoreCase("admin".toLowerCase())) {
            player.setDisplayName("§4Admin §7| §4" + player.getName());
            //player.setPlayerListName("§4Admin §7| §4" + player.getName());
            userGroup = "002Admin";
        } else if (s.equalsIgnoreCase("mod".toLowerCase())) {
            userGroup = "003Mod";
            player.setDisplayName("§cMod §7| §c" + player.getName());
            player.setPlayerListName("§cMod §7| §c" + player.getName());
        } else if (s.equalsIgnoreCase("stammspieler".toLowerCase())) {
            userGroup = "004Stammspieler";
            player.setDisplayName("§6Stammspieler §7| §6" + player.getName());
            player.setPlayerListName("§6Stammspieler §7| §6" + player.getName());
        } else {
            userGroup = "005Spieler";
            player.setDisplayName("§a" + player.getName());
            player.setPlayerListName("§a" + player.getName());
        }
        Bootstrap.getInstance().getMainScoreboard().getTeam(userGroup).addPlayer(player);

        for (final Player all : Bukkit.getOnlinePlayers()) {
            all.setScoreboard(Bootstrap.getInstance().getMainScoreboard());
        }
    }


}

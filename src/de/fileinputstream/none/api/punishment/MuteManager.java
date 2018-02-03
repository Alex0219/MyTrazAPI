package de.fileinputstream.none.api.punishment;

import com.blogspot.debukkitsblog.net.Datapackage;
import de.fileinputstream.none.api.Bootstrap;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MuteManager {

    public static void mute(String UUID, String Playername, String Reason, long seconds, String Dauer, String operator) {
        long end = 0L;
        Playername = Playername.toLowerCase();

        String Player = Bukkit.getOfflinePlayer(Playername).getName();
        if (seconds == -1L) {
            end = -1L;
        } else {
            long currentTime = System.currentTimeMillis();
            long millis = seconds * 1000L;
            end = currentTime + millis;
        }
        Bootstrap.getMysql().update("INSERT INTO Mutes (Spielername, UUID, Ende, Grund, Dauer, Muter) VALUES ('" + Player + "','" + UUID + "','" + end + "','" + Reason + "','" + Dauer + "','" + operator + "')");

        Bootstrap.getInstance().getResilentClient().sendMessage(new Datapackage("SendMessageTeam", "§cSystem §7● §7Der Spieler §e" + Playername + " §7wurde von §e" + getMuter(UUID) + " §7mit dem Grund §e" + Reason + "§7gemutet."));
    }


    public static void unmute(String UUID) {
        Bootstrap.getMysql().update("DELETE FROM Mutes WHERE UUID='" + UUID + "'");
    }

    public static boolean isMuted(String UUID) {
        ResultSet rs = Bootstrap.getMysql().query("SELECT Ende FROM Mutes WHERE UUID='" + UUID + "'");
        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getReason(String UUID) {
        ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Mutes WHERE UUID='" + UUID + "'");
        try {
            if (rs.next()) {
                return rs.getString("Grund");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDauer(String UUID) {
        ResultSet rs = Bootstrap.getMysql().query("SELECT Dauer FROM Mutes WHERE UUID='" + UUID + "'");
        try {
            if (rs.next()) {
                return rs.getString("Dauer");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static long getEnd(String UUID) {
        ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Mutes WHERE UUID='" + UUID + "'");
        try {
            if (rs.next()) {
                return rs.getLong("Ende");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static List<String> getMutedPlayers() {
        List<String> list = new ArrayList();
        ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Mutes");
        try {
            while (rs.next()) {
                list.add(rs.getString("Spielername"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getMuter(String UUID) {
        ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Mutes WHERE UUID='" + UUID + "'");
        try {
            if (rs.next()) {
                return rs.getString("Muter");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§cFehler beim Lesen der Datenbank");
        }
        return "null";
    }

    public static String getRemainingTime(String UUID) {
        long current = System.currentTimeMillis();
        long end = getEnd(UUID);
        if (end == -1L) {
            return "§4PERMANENT";
        }
        long millies = end - current;
        long seconds = 0L;
        long minutes = 0L;
        long hours = 0L;
        long days = 0L;
        long weeks = 0L;
        while (millies > 1000L) {
            millies -= 1000L;
            seconds += 1L;
        }
        while (seconds > 60L) {
            seconds -= 60L;
            minutes += 1L;
        }
        while (minutes > 60L) {
            minutes -= 60L;
            hours += 1L;
        }
        while (hours > 24L) {
            hours -= 24L;
            days += 1L;
        }
        while (days > 7L) {
            days -= 7L;
            weeks += 1L;
        }
        return "§e" + weeks + " Woche(n) " + days + " Tag(e) " + hours + " Stunde(n) " + minutes + " Minute(n) " + seconds + " Sekunde(n) ";
    }

    public String getUUID(String Playername) {
        return Bukkit.getOfflinePlayer(Playername).getUniqueId().toString();
    }

}

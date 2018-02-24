package de.fileinputstream.none.api.punishment;

import de.fileinputstream.none.api.Bootstrap;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BanManager {

    public static void ban(String UUID, String Playername, String Reason, long seconds, String Dauer, String operator, String BanID) {
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
        Bootstrap.getMysql().update("INSERT INTO Bans (Spielername, UUID, Ende, Grund, Dauer, Banner, BanID) VALUES ('" + Player + "','" + UUID + "','" + end + "','" + Reason + "','" + Dauer + "','" + operator + "','" + BanID + "')");
        if (Bukkit.getPlayer(Playername) != null) {
            Bukkit.getPlayer(Playername).kickPlayer("§7Du wurdest gebannt. \n\n§aGrund : " +

                    getReason(UUID) +

                    "\n§eVerbleibende Zeit : " + getRemainingTime(UUID) +

                    "\n§bBanID §e" + getBanID(UUID) +

                    "\nDu kannst im Teamspeak einen Entbannungsantrag stellen : mytraz.net");
        }
    }

    public static void unban(String UUID) {
        Bootstrap.getMysql().update("DELETE FROM Bans WHERE UUID='" + UUID + "'");
    }

    public static boolean isBanned(String UUID) {
        ResultSet rs = Bootstrap.getMysql().query("SELECT Ende FROM Bans WHERE UUID='" + UUID + "'");
        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getReason(String UUID) {
        ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Bans WHERE UUID='" + UUID + "'");
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
        ResultSet rs = Bootstrap.getMysql().query("SELECT Dauer FROM Bans WHERE UUID='" + UUID + "'");
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
        ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Bans WHERE UUID='" + UUID + "'");
        try {
            if (rs.next()) {
                return rs.getLong("Ende");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static List<String> getBannedPlayers() {
        List<String> list = new ArrayList();
        ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Bans");
        try {
            while (rs.next()) {
                list.add(rs.getString("Spielername"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static String getBanner(String UUID) {
        ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Bans WHERE UUID='" + UUID + "'");
        try {
            if (rs.next()) {
                return rs.getString("Banner");
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

    public static String getBanID(String UUID) {
        ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Bans WHERE UUID='" + UUID + "'");
        try {
            if (rs.next()) {
                return rs.getString("BanID");
            }
        } catch (SQLException localSQLException) {
        }
        return "null";
    }

    public static String createbanID(String chars, int length) {
        Random rand = new Random();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < length; i++) {
            buf.append(chars.charAt(rand.nextInt(chars.length())));
        }
        return buf.toString();
    }

    public static String getReasonFromBanID(String banid) {
        ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Bans WHERE BanID='" + banid + "'");
        try {
            if (rs.next()) {
                return rs.getString("Grund");
            }
        } catch (SQLException localSQLException) {
        }
        return null;
    }

    public static String getDauerFromBanID(String banid) {
        ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Bans WHERE BanID='" + banid + "'");
        try {
            if (rs.next()) {
                return rs.getString("Dauer");
            }
        } catch (SQLException localSQLException) {
        }
        return null;
    }

    public String getUUID(String Playername) {
        return Bukkit.getOfflinePlayer(Playername).getUniqueId().toString();
    }
}

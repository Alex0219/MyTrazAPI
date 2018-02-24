package de.fileinputstream.mytraz.api.punishment;

import de.fileinputstream.mytraz.api.Bootstrap;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryManager {

    public void addHistoryEntry(String Typ, String UUID, String Grund, String von, String Dauer, String Name) {
        long end = 0L;
        SimpleDateFormat time = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date currentTime = new Date();
        Bootstrap.getInstance().getMysql().update("INSERT INTO History (Typ, UUID, Grund, Von, Dauer, Datum, Name) VALUES ('" + Typ + "','" + UUID + "','" + Grund + "','" + von + "','" + Dauer + "','" + time.format(currentTime) + "','" + Name + "')");
    }

    public void delHistory(String UUID) {
        Bootstrap.getInstance().getMysql().update("DELETE FROM History WHERE UUID='" + UUID + "'");
    }

    public boolean isRegistered(String UUID) {
        ResultSet rs = Bootstrap.getInstance().getMysql().query("SELECT UUID FROM History WHERE UUID='" + UUID + "'");
        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getReason(String UUID) {
        ResultSet rs = Bootstrap.getInstance().getMysql().query("SELECT * FROM History WHERE UUID='" + UUID + "'");
        try {
            if (rs.next()) {
                return rs.getString("Grund");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getDauer(String UUID) {
        ResultSet rs = Bootstrap.getInstance().getMysql().query("SELECT Dauer FROM History WHERE UUID='" + UUID + "'");
        try {
            if (rs.next()) {
                return rs.getString("Dauer");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public long getEnd(String UUID) {
        ResultSet rs = Bootstrap.getInstance().getMysql().query("SELECT * FROM History WHERE UUID='" + UUID + "'");
        try {
            if (rs.next()) {
                return rs.getLong("Ende");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public List<String> getEntrys(String UUID) {
        List<String> list = new ArrayList();
        ResultSet rs = Bootstrap.getInstance().getMysql().query("SELECT * FROM History WHERE UUID='" + UUID + "'");
        try {
            while (rs.next()) {
                list.add(rs.getString("Typ") + "#Cut#" + rs.getString("Von") + "#Cut#" + rs.getString("Grund") + "#Cut#" + rs.getString("Dauer") + "#Cut#" + rs.getString("Datum") + "#Cut#" + rs.getString("Name") + "#Cut#");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getMuter(String UUID) {
        ResultSet rs = Bootstrap.getInstance().getMysql().query("SELECT * FROM History WHERE UUID='" + UUID + "'");
        try {
            if (rs.next()) {
                return rs.getString("Muter");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("�cFehler beim Lesen der Datenbank");
        }
        return "null";
    }

    public String getRemainingTime(String UUID) {
        long current = System.currentTimeMillis();
        long end = getEnd(UUID);
        if (end == -1L) {
            return "�4PERMANENT";
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

package de.fileinputstream.none.api.rank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.Main;
import org.bukkit.scheduler.BukkitRunnable;

import de.fileinputstream.none.api.Bootstrap;

public class RankManager {
	
	public static HashMap<String,String> ranks = new HashMap(); //First is UUID, second is Rank

    public static boolean playerExists(String uuid)
    {
        try
        {
            ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Rank WHERE UUID= '" + uuid + "'");
            if (rs.next()) {
                return rs.getString("UUID") != null;
            }
            return false;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static void createPlayer(String uuid)
    {

        if (!playerExists(uuid)) {
            Bootstrap.getMysql().update("INSERT INTO Rank(UUID, RANG) VALUES ('" + uuid + "', 'SPIELER');");
            if(!ranks.containsKey(uuid)) {
                ranks.put(uuid,"spieler");
            }
        }
    }

    public static String getRank(String uuid)
    {
            String str = "";
            if (playerExists(uuid)) {
                try {
                    ResultSet rs = Bootstrap.getMysql().query("SELECT * FROM Rank WHERE UUID= '" + uuid + "'");
                    if ((rs.next()) && (rs.getString("RANG") == null)) {
                    }
                    str = rs.getString("RANG");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            return str;
        }


    public static void setRank(String uuid, String rank)
    {
        if (playerExists(uuid))
        {
            Bootstrap.getMysql().update("UPDATE Rank SET RANG= '" + rank.toString() + "' WHERE UUID= '" + uuid + "';");

        }
        else
        {
            createPlayer(uuid);
            setRank(uuid, rank);
        }
    }

    public static boolean hasRank(String uuid, String rank) {
       if(getRank(uuid).equalsIgnoreCase(rank)) {
           return true;
       } else {
           return false;
       }
    }
}

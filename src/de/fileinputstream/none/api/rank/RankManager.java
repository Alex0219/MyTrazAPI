package de.fileinputstream.none.api.rank;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.user.MyTrazUser;
import org.bson.Document;


import de.fileinputstream.none.api.Bootstrap;


public class RankManager {


    public static HashMap<String, String> ranks = new HashMap(); //First is UUID, second is Rank

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

        MyTrazUser user = new MyTrazUser(UUIDFetcher.getName(UUID.fromString(uuid)));
        if (user.getDocument() != null) {

        }

        String playerRequest = user.getDocument().get("Rank").toString();
        System.out.println("Succesfully requested player information and got something back.");

        return playerRequest;
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

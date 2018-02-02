package de.fileinputstream.none.api.user;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import de.fileinputstream.none.api.Bootstrap;
import de.fileinputstream.none.api.cache.UUIDFetcher;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * User: Alexander<br/>
 * Date: 07.01.2018<br/>
 * Time: 19:16<br/>
 * MIT License
 * <p>
 * Copyright (c) 2017 Alexander Fiedler
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use and modify without distributing the software to anybody else,
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p>
 * <p>
 * MIT Lizenz
 * Copyright (c) 2017 Alexander Fiedler
 * Hiermit wird unentgeltlich jeder Person, die eine Kopie der Software und der zugehörigen Dokumentationen (die "Software") erhält, die Erlaubnis erteilt, sie uneingeschränkt zu nutzen, inklusive und ohne Ausnahme mit dem Recht, sie zu verwenden, zu verändern und Personen, denen diese Software überlassen wird, diese Rechte zu verschaffen, außer sie zu verteilen unter den folgenden Bedingungen:
 * <p>
 * Der obige Urheberrechtsvermerk und dieser Erlaubnisvermerk sind in allen Kopien oder Teilkopien der Software beizulegen.
 * <p>
 * DIE SOFTWARE WIRD OHNE JEDE AUSDRÜCKLICHE ODER IMPLIZIERTE GARANTIE BEREITGESTELLT, EINSCHLIEßLICH DER GARANTIE ZUR BENUTZUNG FÜR DEN VORGESEHENEN ODER EINEM BESTIMMTEN ZWECK SOWIE JEGLICHER RECHTSVERLETZUNG, JEDOCH NICHT DARAUF BESCHRÄNKT. IN KEINEM FALL SIND DIE AUTOREN ODER COPYRIGHTINHABER FÜR JEGLICHEN SCHADEN ODER SONSTIGE ANSPRÜCHE HAFTBAR ZU MACHEN, OB INFOLGE DER ERFÜLLUNG EINES VERTRAGES, EINES DELIKTES ODER ANDERS IM ZUSAMMENHANG MIT DER SOFTWARE ODER SONSTIGER VERWENDUNG DER SOFTWARE ENTSTANDEN.
 */
public class MyTrazUser {


    String name;
    Player player;
    List<World> worldList = new ArrayList<World>();

    public MyTrazUser(String name) {
        this.name = name;
        this.player = Bukkit.getPlayer(name);
    }

    public boolean hasPlot() {
        //Not finished
        return new File("/BuildEvent/" + getUUID() + ".yml").exists();
    }

    public UUID getUUID() {
        //We can do this request because the Fetcher class caches the uuid's for us.
        return UUIDFetcher.getUUID(this.name);
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<String> getWorlds() {

        DBCursor playerFind = Bootstrap.getMongoManager().getPlayers().find(new BasicDBObject("UUID", getUUID()));
        DBObject found = playerFind.next();
        if (found == null) {
            System.out.println("Backend -> User not found! We do not create the user because there could be an error!");

        } else {
            return (ArrayList) found.get("Worlds");
        }
        return null;
    }

    public void createMyTrazUser() {

        String uuid = getUUID().toString();

        if (Bootstrap.getMongoManager().getPlayers().find(new BasicDBObject("uuid", uuid)).hasNext()) {
            DBObject found = Bootstrap.getMongoManager().getPlayers().find(new BasicDBObject("uuid", uuid)).next();

            if (found == null) {

                List<String> playerWorlds = new ArrayList<String>(); //Player can't have any worlds at the creation so we will leave that null

                //Building up the Document

                DBObject playerRequest = new BasicDBObject("UUID", uuid)
                        .append("uuid", uuid)
                        .append("name", UUIDFetcher.getName(UUID.fromString(uuid)))
                        .append("registerTimestamp", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()))
                        .append("worlds", playerWorlds)
                        .append("logins", 1);
                //And  flush!!!!
                Bootstrap.getMongoManager().getPlayers().insert(playerRequest);
                //We print the just created user to see if something went wrong.
                System.out.println("Backend -> Created user with the following entries: " +
                        playerRequest.toString());

            } else {
                System.out.println("Backend -> User already exists. OK.");
                return;
            }
        } else {
            List<String> playerWorlds = new ArrayList<String>(); //Player can't have any worlds at the creation so we will leave that null

            //Building up the Document

            DBObject playerRequest = new BasicDBObject("UUID", uuid)
                    .append("uuid", uuid)
                    .append("name", UUIDFetcher.getName(UUID.fromString(uuid)))
                    .append("registerTimestamp", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()))
                    .append("worlds", playerWorlds)
                    .append("logins", 1);
            //And  flush!!!!
            Bootstrap.getMongoManager().getPlayers().insert(playerRequest);
            //We print the just created user to see if something went wrong.
            System.out.println("Backend -> Created user with the following entries: " +
                    playerRequest.toString());
        }


    }

    public DBObject getDocument() {
        DBCursor playerFind = Bootstrap.getMongoManager().getPlayers().find(new BasicDBObject("UUID", getUUID()));
        if (playerFind == null) {
            if (playerFind.hasNext()) {
                DBObject found = playerFind.next();
                if (found == null) {
                    System.out.println("Backend -> User not found! We do not create the user because there could be an error!");
                    return null;
                } else {
                    return found;
                }
            }
            System.out.println("Backend -> Could not get request user info! Report!");
            return null;
        }
        System.out.println("Backend -> Could not get request user info! Report!");
        return null;
    }



}

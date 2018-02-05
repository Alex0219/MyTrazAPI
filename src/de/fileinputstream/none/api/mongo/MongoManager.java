package de.fileinputstream.none.api.mongo;


import com.mongodb.*;
import de.fileinputstream.none.api.Bootstrap;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

/**
 * User: Alexander<br/>
 * Date: 29.01.2018<br/>
 * Time: 16:55<br/>
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
public class MongoManager {

    private final String hostname;
    private final int port;

    private MongoClient client;
    private DB database;

    private DBCollection playerWorlds;
    private DBCollection players;
    private DBCollection settings;


    public MongoManager(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void connect() {
        this.client = new MongoClient(hostname, port);


        this.database = this.client.getDB("MyTraz");


        this.playerWorlds = this.database.getCollection("playerWorlds");
        this.players = this.database.getCollection("Players");
        this.settings = this.database.getCollection("Settings");
        this.database.createCollection("Players", null);
        System.out.println("Backend -> Connected to mongo! Let the mongo begin!");


        new BukkitRunnable() {
            @Override
            public void run() {


            }
        }.runTaskAsynchronously(Bootstrap.getInstance());


    }

    public DBCollection getPlayers() {
        return players;
    }

    public DBCollection getPlayerWorlds() {
        return playerWorlds;
    }

    public DBCollection getSettings() {
        return settings;
    }

    public void userExists(String uuid, Consumer<Boolean> consumer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                while (!Bootstrap.getMongoManager().getPlayers().find(new BasicDBObject("uuid", uuid)).hasNext()) {
                    consumer.accept(false);
                }
                DBObject document = Bootstrap.getMongoManager().getPlayers().find(new BasicDBObject("uuid", uuid)).next();

                if (document == null) {
                    consumer.accept(false);
                } else {
                    consumer.accept(true);
                }
            }
        }.runTaskAsynchronously(Bootstrap.getInstance());
    }

    public void setSpawn(Location location) {
        double x = location.getX();
        double z = location.getZ();
        double y = location.getY();
        float yaw = location.getYaw();
        float pitch = location.getPitch();


    }


}
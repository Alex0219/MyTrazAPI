package de.fileinputstream.none.api.user;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import de.fileinputstream.none.api.Bootstrap;
import de.fileinputstream.none.api.cache.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

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

    public String getUUID() {
        //We can do this request because the Fetcher class caches the uuid's for us.
        System.out.println(UUIDFetcher.getUUID(this.name).toString());
        return UUIDFetcher.getUUID(this.name).toString();
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<String> getWorlds() {


        return null;
    }

    public void createMyTrazUser() {

        String uuid = getUUID().toString();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Bootstrap.getMongoManager().userExists(uuid, new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (aBoolean == false) {
                            List<String> playerWorlds = new ArrayList<String>(); //Player can't have any worlds at the creation so we will leave that null

                            //Building up the Document

                            DBObject playerRequest = new BasicDBObject("uuid", uuid)
                                    .append("name", UUIDFetcher.getName(UUID.fromString(uuid)))
                                    .append("registerTimestamp", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()))
                                    .append("rank", "spieler")
                                    .append("worlds", playerWorlds)
                                    .append("logins", 1);
                            //And  flush!!!!


                            Bootstrap.getMongoManager().getPlayers().insert(playerRequest);


                            //We print the just created user to see if something went wrong.
                        } else {
                            return;
                        }
                    }
                });
            }
        });


    }

    public void getDocument(Consumer<BasicDBObject> consumer) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Bootstrap.getMongoManager().userExists(getUUID(), new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) {
                        if (aBoolean == true) {
                            DBObject document = Bootstrap.getMongoManager().getPlayers().find(new BasicDBObject("uuid", getUUID())).next();
                            consumer.accept((BasicDBObject) document);
                        } else {
                            System.out.println("Backend -> I am sad. I couldn't lookup a document for: " + getUUID());
                        }
                    }
                });
            }
        });


    }
}









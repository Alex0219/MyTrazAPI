package de.fileinputstream.redisbuilder.user;

import de.fileinputstream.redisbuilder.RedisBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * User: Alexander<br/>
 * Date: 04.02.2018<br/>
 * Time: 17:36<br/>
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
public class DBUser {
    /*
    Diese Klasse verwaltet den @DBUser, auch bekannt als Spieler.
    Sämtliche Abfragen werden durch diese Klasse verwaltet.
     */

    String uuid;
    String name;
    Player player;

    public DBUser(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
        if (Bukkit.getPlayer(name) != null) {
            this.player = Bukkit.getPlayer(uuid);
        }
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    //Existiert User
    public boolean userExists() {
        if (RedisBuilder.getInstance().getJedis().exists("uuid:" + getUuid())) {
            return true;
        } else {
            return false;
        }
    }

    //User wird erstellt
    public void createUser() {

        List<String> worlds = new ArrayList<String>();
        List<String> residentWorlds = new ArrayList<String>();
        String joinedWorld = Arrays.toString(worlds.toArray());
        String joinedResidentWorlds = Arrays.toString(residentWorlds.toArray());
        RedisBuilder.getInstance().getJedis().hset("uuid:" + getUuid(), "name", getName());
        RedisBuilder.getInstance().getJedis().hset("uuid:" + getUuid(), "registertimestamp", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        RedisBuilder.getInstance().getJedis().hset("uuid:" + getUuid(), "logins", "1");
        RedisBuilder.getInstance().getJedis().hset("uuid:" + getUuid(), "worlds", joinedWorld);
        RedisBuilder.getInstance().getJedis().hset("uuid:" + getUuid(), "residentworlds", joinedResidentWorlds);
        RedisBuilder.getInstance().getJedis().hset("uuid:" + getUuid(), "rank", "spieler");
        RedisBuilder.getInstance().getJedis().hset("uuid:" + getUuid(), "banned", "false");
        System.out.println("Created user with uuid:" + uuid);

    }

    public Player getPlayer() {
        return player;
    }
}


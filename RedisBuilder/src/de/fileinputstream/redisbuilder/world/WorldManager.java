package de.fileinputstream.redisbuilder.world;

import de.fileinputstream.redisbuilder.RedisBuilder;
import de.fileinputstream.redisbuilder.user.DBUser;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * User: Alexander<br/>
 * Date: 06.01.2018<br/>
 * Time: 14:37<br/>
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
public class WorldManager {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public WorldManager() {

    }

    /**
     * @param {@link DBUser} Der Benutzer in der Datenbank, der die Welt erhalten soll.
     * @apiNote
     */
    public void createWorld(DBUser user) {

        //WorldCreator wc = new WorldCreator()
        ArrayList<String> worldResidents = new ArrayList<String>();
        String worldID = getNewWorldID();
        Bukkit.createWorld(new WorldCreator(getNewWorldID()));
        user.getPlayer().sendMessage("§aDeine Welt " + "§c" + worldID + " §awird nun erstellt. Wenn die Erstellung abgeschlossen ist, wirst du automatisch in deine Welt teleportiert.");

        RedisBuilder.getInstance().getJedis().hset("world:" + worldID, "timestamp", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        RedisBuilder.getInstance().getJedis().hset("world:" + worldID, "owner", user.getName());
        RedisBuilder.getInstance().getJedis().hset("world:" + worldID, "residents", Arrays.toString(worldResidents.toArray()));

        Bukkit.getScheduler().runTaskLater(RedisBuilder.getInstance(), new BukkitRunnable() {
            @Override
            public void run() {
                //Teleport the player to the spawn location
                user.getPlayer().teleport(Bukkit.getWorld(worldID).getSpawnLocation());

            }
        }, 30);

    }

    /**
     * @return {@link String} Gibt einen String zurück, der als WeltenID verwendet wird.
     */
    public String getNewWorldID() {
        return "#" + randomString(7);
    }

    //Random string for world id
    public String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        return sb.toString();
    }

    //Überprüft, ob die Welt existiert

    /**
     * @param worldID WeltenID
     * @return boolean
     */
    public boolean worldExists(String worldID) {
        if (RedisBuilder.getInstance().getJedis().exists("world:" + worldID)) {
            return true;
        } else {
            return false;
        }
    }

    //Weltenbewohner wird hinzugefügt

    /**
     * @param worldID WeltenID
     * @param uuid    ID des Spielers
     */
    public void addResident(String worldID, String uuid) {
        if (worldExists(worldID)) {
            String residents = RedisBuilder.getInstance().getJedis().hget("world:" + worldID, "residents");
            ArrayList<String> worldResidents = new ArrayList<String>(Arrays.asList(residents.split(",")));
            if (!worldResidents.contains(uuid)) {
                worldResidents.add(uuid);
                RedisBuilder.getInstance().getJedis().hset("world:" + worldID, "residents", Arrays.toString(worldResidents.toArray()));
                System.out.println("Backend -> Added resident:" + uuid + " to world id:" + worldID);
            }
        }
    }

    /**
     * @param worldID
     * @return {@link ArrayList} Liste mit den Bewohnern von der angegebenen WorldID
     */
    public ArrayList<String> getWorldResidents(String worldID) {
        if (worldExists(worldID)) {
            String residents = RedisBuilder.getInstance().getJedis().hget("world:" + worldID, "residents");
            ArrayList<String> worldResidents = new ArrayList<String>(Arrays.asList(residents.split(",")));
            return worldResidents;
        } else {
            System.out.println("Backend -> Could not find user world: " + worldID);
            return null;
        }

    }

    public boolean playerHasWorld(String uuid) {
        String worldString = RedisBuilder.getInstance().getJedis().hget("uuid:" + uuid, "worlds");
        ArrayList<String> playerWorlds = new ArrayList<String>(Arrays.asList(worldString.split(",")));
        if (playerWorlds.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }


}
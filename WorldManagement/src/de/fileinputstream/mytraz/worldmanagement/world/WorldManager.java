package de.fileinputstream.mytraz.worldmanagement.world;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public HashMap<String, String> worldInvites = new HashMap<String, String>();

    public WorldManager() {

    }

    /**
     * Erstellt eine Welt für einen Benutzer. Hier wird überprüft, ob der Benutzer schon eine Welt hat.
     * Nach 30 Millisekunden wir der Spieler dann in seine Welt teleportiert.
     *
     * @param {@link DBUser} Der Benutzer in der Datenbank, der die Welt erhalten soll.
     */
    public void createWorld(String name, String uuid) {
        //WorldCreator wc = new WorldCreator()
        List<String> worlds = new ArrayList<String>();
        List<String> residentWorlds = new ArrayList<String>();

        String joinedWorld = Arrays.toString(worlds.toArray());
        String joinedResidentWorlds = Arrays.toString(residentWorlds.toArray());
        ArrayList<String> worldResidents = new ArrayList<String>();
        String worldID = getNewWorldID();
        if (!worlds.contains(worldID)) {
            worlds.add(worldID);
        }
        Bukkit.createWorld(new WorldCreator(worldID));
        Bukkit.getPlayer(name).sendMessage("§aDeine Welt " + "§c" + worldID + " §awird nun erstellt. Wenn die Erstellung abgeschlossen ist, wirst du automatisch in deine Welt teleportiert.");
        Bootstrap.getInstance().getJedis().hset("world:" + worldID, "timestamp", new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        Bootstrap.getInstance().getJedis().hset("world:" + worldID, "owner", name);
        Bootstrap.getInstance().getJedis().hset("world:" + worldID, "residents", Arrays.toString(worldResidents.toArray()));


        //Teleport the player to the spawn location
        Bukkit.getPlayer(name).teleport(Bukkit.getWorld(worldID).getSpawnLocation());
        Bukkit.getWorld(worldID).setAutoSave(true);
        Bukkit.getWorld(worldID).save();
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

        if (Bootstrap.getInstance().getJedis().exists("world:" + worldID)) {
            return true;
        } else {
            return false;
        }
    }

    //Weltenbewohner wird hinzugefügt

    /**
     * Fügt einen Spieler zu einer Welt hinzu, sofern er noch nicht eingetragen ist.
     * @param worldID WeltenID
     * @param uuid    ID des Spielers
     */
    public void addResident(String worldID, String uuid) {

        if (worldExists(worldID)) {
            String residents = Bootstrap.getInstance().getJedis().hget("world:" + worldID, "residents");
            ArrayList<String> worldResidents = new ArrayList<String>(Arrays.asList(residents));
            if (!worldResidents.contains(uuid)) {
                worldResidents.add(uuid);
                Bootstrap.getInstance().getJedis().hset("world:" + worldID, "residents", Arrays.toString(worldResidents.toArray()));
                System.out.println("Backend -> Added resident:" + uuid + " to world id:" + worldID);
            }
        }
    }

    /**
     * Entfernt einen Spieler aus der Welt sofern er eingetragen ist
     *
     * @param worldID WeltenID
     * @param uuid    ID des Spielers
     */
    public void removeResident(String worldID, String uuid) {
        if (worldExists(worldID)) {
            String residents = Bootstrap.getInstance().getJedis().hget("world:" + worldID, "residents");
            ArrayList<String> worldResidents = new ArrayList<String>(Arrays.asList(residents));
            if (worldResidents.contains(uuid)) {
                worldResidents.remove(uuid);
                Bootstrap.getInstance().getJedis().hset("world:" + worldID, "residents", Arrays.toString(worldResidents.toArray()));
                System.out.println("Backend -> Removed resident:" + uuid + " from world id:" + worldID);
            }
        }
    }

    /**
     * @param worldID
     * @return {@link ArrayList} Liste mit den Bewohnern von der angegebenen WorldID
     */
    public ArrayList<String> getWorldResidents(String worldID) {

        if (worldExists(worldID)) {
            String residents = Bootstrap.getInstance().getJedis().hget("world:" + worldID, "residents");
            ArrayList<String> worldResidents = new ArrayList<String>(Arrays.asList(residents));
            return worldResidents;
        } else {
            System.out.println("Backend -> Could not find user world: " + worldID);
            return null;
        }

    }

    /**
     * @param uuid
     * @return {@link boolean} Überprüft, ob ein Spieler eine Welt besitzt.
     * Dabei ist es egal, wieviele Welten der User hat.
     */
    public boolean playerHasWorld(String uuid) {

        String worldString = Bootstrap.getInstance().getJedis().hget("uuid:" + uuid, "worlds");
        ArrayList<String> playerWorlds = new ArrayList<String>(Arrays.asList(worldString));
        System.out.println(playerWorlds.toString());
        if (playerWorlds.toString().replace("[", "").replace("]", "").equalsIgnoreCase("")) {
            System.out.println("Backend -> Player hasn't got any worlds.");
            return false;
        } else {
            return true;
        }
    }

    public boolean isResidentInWorld(String uuid, String world) {
        String residents = Bootstrap.getInstance().getJedis().hget("world:" + world, "residents");
        ArrayList<String> worldResidents = new ArrayList<String>(Arrays.asList(residents));
        if (worldResidents.contains(uuid)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasWorld(String uuid) {

        if (Bootstrap.getInstance().getJedis().hget("uuid:" + uuid, "hasworld").equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    public String getWorld(String uuid) {
        Bootstrap.getInstance().getJedis().select(Bootstrap.getInstance().getConfig().getInt("Redis-DB"));
        if (Bootstrap.getInstance().getWorldManager().playerHasWorld(uuid)) {
            String worldString = Bootstrap.getInstance().getJedis().hget("uuid:" + uuid, "worlds");
            System.out.println(worldString);
            ArrayList<String> playerWorlds = new ArrayList(Arrays.asList(new String[]{worldString}));
            String world = ((String) playerWorlds.get(0)).replace("[", "").replace("]", "");

            return world;
        }
        return "";
    }


}
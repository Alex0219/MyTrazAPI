package de.fileinputstream.redisbuilder.user;


import de.fileinputstream.redisbuilder.RedisBuilder;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

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


    /**
     * @param uuid
     * @return {@link boolean} Überprüft, ob ein Spieler eine Welt besitzt.
     * Dabei ist es egal, wieviele Welten der User hat.
     */

    public boolean hasWorld(String uuid) {

        if (RedisBuilder.getInstance().getJedis().hget("uuid:" + uuid, "hasworld").equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    public String getWorld(String uuid) {

        if (hasWorld(uuid)) {
            String worldString = RedisBuilder.getInstance().getJedis().hget("uuid:" + uuid, "worlds");
            System.out.println(worldString);
            ArrayList<String> playerWorlds = new ArrayList(Arrays.asList(new String[]{worldString}));
            String world = ((String) playerWorlds.get(0)).replace("[", "").replace("]", "");
            return world;
        }
        return "";
    }
}
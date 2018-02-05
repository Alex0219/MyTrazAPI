package de.fileinputstream.none.api.handling;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import de.fileinputstream.none.api.Bootstrap;
import de.fileinputstream.none.api.user.MyTrazUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Random;


/**
 * User: Alexander<br/>
 * Date: 29.01.2018<br/>
 * Time: 19:26<br/>
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
public class JoinHandler {

    public void handleJoin(MyTrazUser user) {

        user.getPlayer().sendMessage("§8Willkommen auf §cMyTraz.net! §7Bitte beachte, dass wir in der Beta sind und Fehler auftreten können.");

        user.getPlayer().sendMessage("§cDies ist nur eine vorübergehende Lobby, da unsere Hauptlobby noch nicht fertig ist.");

        user.getPlayer().sendTitle("§7§lWillkommen auf", "§l§6MyTraz.net");
        ActionBar.sendActionBarTime(user.getPlayer(), "§b§7MyTraz ist ein Projekt von §6www.mediolutec.de!", 600);
        handleBroadcaster(user.getPlayer());
        user.getPlayer().performCommand("spawn");
    }

    public void handleBroadcaster(Player player) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bootstrap.getInstance(), new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                switch (r.nextInt(2)) {
                    case 1:
                        player.sendMessage("§c§lMyTraz ist ein Projekt von §l§6www.mediolutec.de!");
                        break;
                    case 2:
                        player.sendMessage("§c§lDie ersten 40 Spieler bekommen bei 85 Stunden §c§lLifetime Premium §7§lkostenlos");
                        break;
                    case 0:
                        player.sendMessage("§c§lDie ersten 40 Spieler bekommen bei 85 Stunden §c§lLifetime Premium §7§lkostenlos");
                        break;

                }

            }
        }, 3500L, 3500L);
    }
}
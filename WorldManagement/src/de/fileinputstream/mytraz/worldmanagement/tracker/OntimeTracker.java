package de.fileinputstream.mytraz.worldmanagement.tracker;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * User: Alexander<br/>
 * Date: 26.02.2018<br/>
 * Time: 19:51<br/>
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
public class OntimeTracker {

    public OntimeTracker() {

    }

    public void startCounter() {
        Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(Bootstrap.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (final Player all : Bukkit.getOnlinePlayers()) {
                    if (Bootstrap.getInstance().getConfig().getString("Spieler." + all.getName() + ".online") != null) {
                        Bootstrap.getInstance().getConfig().set("Spieler." + all.getName() + ".online", (Bootstrap.getInstance().getConfig().getLong("Spieler." + all.getName() + ".online") + 1L));
                    } else {
                        Bootstrap.getInstance().getConfig().set("Spieler." + all.getName() + ".online", 0);
                    }

                    Bootstrap.getInstance().saveConfig();
                }
            }
        }, 20L, 20L);
    }

    public String getOnlinetime(final Player p) {

        long seconds = Bootstrap.getInstance().getConfig().getLong("Spieler." + p.getName() + ".online");
        long minutes = 0L;
        long hours = 0L;
        long days = 0L;
        long weeks = 0L;
        while (seconds > 60L) {
            seconds -= 60L;
            ++minutes;
        }
        while (minutes > 60L) {
            minutes -= 60L;
            ++hours;
        }
        while (hours > 24L) {
            hours -= 24L;
            ++days;
        }
        while (days > 7L) {
            days -= 7L;
            ++weeks;
        }
        while (weeks > 7L) {
            days -= 7L;
        }
        if (weeks != 0L) {
            return "§c" + weeks + " §7Woche(n) §c" + "§c" + days + " §7Tag(e) §c" + hours + " §7Stunde(n) §c" + minutes + " §7Minute(n) §c" + seconds + " §7Sekunde(n)";
        }
        if (days != 0L) {
            return "§c" + days + " §7Tag(e) §c" + hours + " §7Stunde(n) §c" + minutes + " §7Minute(n) §c" + seconds + " §7Sekunde(n)";
        }
        if (hours != 0L) {
            return "§c" + hours + " §7Stunde(n) §c" + minutes + " §7Minute(n) §c" + seconds + " §7Sekunde(n)";
        }
        if (minutes != 0L) {
            return "§c" + minutes + " §7Minute(n) §c" + seconds + " §7Sekunde(n)";
        }
        if (seconds != 0L) {
            return "§c" + seconds + " §7Sekunde(n)";
        }
        return "§c" + weeks + " §7Woche(n) §c" + "§c" + days + " §7Tag(e) §c" + hours + " §7Stunde(n) §c" + minutes + " §7Minute(n) §6" + seconds + " §7Sekunde(n)";

    }
}

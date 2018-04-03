package de.fileinputstream.mytraz.bungee.listeners;

import de.fileinputstream.mytraz.bungee.manager.MuteManager;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Alexander<br/>
 * Date: 04.03.2018<br/>
 * Time: 20:04<br/>
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
public class ListenerChat implements Listener {

    public List<String> b = new ArrayList<>();
    public ArrayList<String> insult = new ArrayList<>();

    public ListenerChat() {
        b.add("ficker");
        b.add("fotze");
        b.add("abspritzen");
        b.add("arschficker");
        b.add("anus");
        b.add("arschgefickter");
        b.add("nazi");
        b.add("hitler");
        b.add("fick dich");
        b.add("analspritzer");
        b.add("orgasmus");
        b.add("motherfucker");
        b.add("ich hab deine mutter gefickt");
        b.add("schamhaarasierer");
        b.add("fotzenlecker");
        b.add(".de");
        b.add(".com");
        b.add(".xyz");
        b.add(".net");
    }

    @EventHandler
    public void onChat(ChatEvent e) {
        ProxiedPlayer p = (ProxiedPlayer) e.getSender();

        String msg = e.getMessage();
        for (String s : b) {

            if (msg.equalsIgnoreCase(s)) {
                if (insult.contains(p.getName())) {
                    MuteManager.Mute(p.getName(), "Beleidugung/Linkweitergabe im Chat", "Cloud", 36000);
                    insult.remove(p.getName());
                    e.setMessage("MyTraz ist der tollste Server auf der Welt!");

                } else {
                    p.sendMessage("§bCloud §7» §cBitte beleidige nicht oder schreibe keine Links!");
                    p.sendMessage("§bCloud §7» Solltest du dies nochmal schreiben, wirst du gemutet!");
                    e.setMessage("MyTraz ist der tollste Server auf der Welt!");
                    insult.add(p.getName());


                }


            }
        }
        if (MuteManager.isMuted(p.getName())) {
            long currentm = System.currentTimeMillis();
            long current = currentm / 1000L;
            long end = MuteManager.getEnd(p.getName());
            if ((end < current) && (end != -1L)) {
                MuteManager.unMute(p.getName(),
                        "Cloud");
                e.setCancelled(false);
            } else if (!e.getMessage().startsWith("/")) {
                e.setCancelled(true);


                p.sendMessage("§bCloud §7» §cDu wurdest aus dem Chat gebannt!");
                p.sendMessage("§bCloud §7» Dauer: " + MuteManager.getRemainingTime(p.getName()));
                p.sendMessage("§bCloud §7» §cGrund: " + MuteManager.getReason(p.getName()));
            }
        }
    }
}

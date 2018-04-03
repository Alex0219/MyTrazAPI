package de.fileinputstream.mytraz.ts3module;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: Alexander<br/>
 * Date: 20.02.2018<br/>
 * Time: 16:48<br/>
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
public class AFKMover {
    public static HashMap<String, Long> AFK = new HashMap();
    public static HashMap<String, Boolean> Moved = new HashMap();
    public static HashMap<String, Integer> Channel = new HashMap();
    public static int Away = 1000;

    public static void start() {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                for (Client clients : Bootstrap.api.getClients()) {
                    if (!clients.isServerQueryClient()) {
                        if ((clients.isAway()) || (clients.isInputMuted()) || (clients.isOutputMuted())) {
                            if (!AFKMover.Moved.containsKey(clients.getUniqueIdentifier())) {
                                if (!AFKMover.AFK.containsKey(clients.getUniqueIdentifier())) {
                                    AFKMover.AFK.put(clients.getUniqueIdentifier(), Long.valueOf(System.currentTimeMillis()));
                                }
                                if (clients.isInServerGroup(39)) {
                                    return;
                                } else {
                                    long current = AFKMover.AFK.get(clients.getUniqueIdentifier()).longValue();
                                    if (System.currentTimeMillis() - current >= AFKMover.Away) {
                                        AFKMover.Moved.put(clients.getUniqueIdentifier(), Boolean.valueOf(true));
                                        AFKMover.Channel.put(clients.getUniqueIdentifier(), Integer.valueOf(clients.getChannelId()));
                                        AFKMover.AFK.remove(clients.getUniqueIdentifier());
                                        Bootstrap.api.sendPrivateMessage(clients.getId(), "Du wurdest in den AFK Channel verschoben");

                                        if (clients.isInServerGroup(242)) {
                                            Bootstrap.api.moveClient(clients.getId(), 508);
                                        } else {
                                            Bootstrap.api.moveClient(clients.getId(), 538);
                                        }

                                    }
                                }
                            }
                        } else {
                            AFKMover.AFK.remove(clients.getUniqueIdentifier());
                            if ((AFKMover.Moved.containsKey(clients.getUniqueIdentifier())) &&
                                    (AFKMover.Moved.get(clients.getUniqueIdentifier()).booleanValue())) {
                                AFKMover.Moved.remove(clients.getUniqueIdentifier());
                                AFKMover.Channel.remove(clients.getUniqueIdentifier());
                            }
                        }
                    }
                }
            }
        }, 1000L, 5000L);
    }
}




package de.fileinputstream.mytraz.ts3module;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

/**
 * User: Alexander<br/>
 * Date: 20.02.2018<br/>
 * Time: 16:43<br/>
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
public class Bootstrap {

    public static TS3Config config = new TS3Config();
    public static TS3Query query = new TS3Query(config);
    public static TS3Api api = query.getApi();


    public static void main(String[] args) {
        config.setHost("ts.mytraz.net");
        query.connect();

        api.login("serveradmin", "yJvNgSYu");

        api.selectVirtualServerById(6);
        api.setNickname("MyTraz-Bot");
        api.broadcast("Bot nun aktiv");
        api.registerAllEvents();

        AFKMover.start();
        api.addTS3Listeners(new TS3Listener() {
            public void onTextMessage(TextMessageEvent e) {
            }

            public void onServerEdit(ServerEditedEvent e) {
            }

            public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent e) {
            }

            public void onClientMoved(ClientMovedEvent e) {
                int id = e.getTargetChannelId();
                if (id == 518) {
                    for (Client clients : Bootstrap.api.getClients()) {
                        if (clients.isInServerGroup(213) || clients.isInServerGroup(239)) {
                            Bootstrap.api.sendPrivateMessage(clients.getId(), "Jemand möchte sich bewerben.");
                        }
                    }
                }
                if (id == 527) {
                    for (Client clients : Bootstrap.api.getClients()) {
                        if ((clients.isInServerGroup(216)) || (clients.isInServerGroup(217)) || (clients.isInServerGroup(213))) {
                            Bootstrap.api.sendPrivateMessage(clients.getId(), "Jemand benötigt Hilfe im Support");
                        }
                    }
                }
            }

            public void onClientLeave(ClientLeaveEvent e) {
                Client client = Bootstrap.api.getClientByNameExact(e.getInvokerName(), true);
                AFKMover.AFK.remove(client.getUniqueIdentifier());
                if (AFKMover.Channel.containsKey(client.getUniqueIdentifier())) {
                    AFKMover.Channel.containsKey(client.getUniqueIdentifier());
                }
                AFKMover.Moved.remove(client.getUniqueIdentifier());
            }

            public void onClientJoin(ClientJoinEvent e) {
            }

            public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
            }

            public void onChannelMoved(ChannelMovedEvent e) {
            }

            public void onChannelEdit(ChannelEditedEvent e) {
            }

            public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent e) {
            }

            public void onChannelDeleted(ChannelDeletedEvent e) {
            }

            public void onChannelCreate(ChannelCreateEvent e) {
            }
        });
    }
}

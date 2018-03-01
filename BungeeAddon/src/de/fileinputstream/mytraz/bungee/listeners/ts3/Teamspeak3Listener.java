package de.fileinputstream.mytraz.bungee.listeners.ts3;

import com.github.theholywaffle.teamspeak3.api.event.*;
import de.fileinputstream.mytraz.bungee.api.TeamSpeakAPI;
import de.fileinputstream.mytraz.bungee.sql.MySQL;

import java.security.SecureRandom;
import java.util.HashMap;

/**
 * User: Alexander<br/>
 * Date: 26.02.2018<br/>
 * Time: 17:23<br/>
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
public class Teamspeak3Listener implements TS3Listener {


    public static HashMap<String, String> verify = new HashMap();
    private final String AB = "01ABCDWXYZabcdefghixyzFGVertutz7346tg54uzfftcw";
    private SecureRandom rnd = new SecureRandom();

    public void onTextMessage(TextMessageEvent textMessageEvent) {
    }

    public void onClientJoin(ClientJoinEvent e) {
        MySQL.connect();

        if (!TeamSpeakAPI.api.getClientByUId(e.getUniqueClientIdentifier()).isInServerGroup(209)) {
            if (!TeamSpeakAPI.isVerified(e.getUniqueClientIdentifier())) {
                String key = randomString(6) + e.getUniqueClientIdentifier();
                TeamSpeakAPI.api.sendPrivateMessage(e.getClientId(), "Du bist noch nicht mit dem Minecraft Server gelinkt. Bitte verwende in Minecraft: /ts " + key);
                verify.put(key, e.getUniqueClientIdentifier());
                TeamSpeakAPI.api.removeClientFromServerGroup(34, e.getClientDatabaseId());
            } else {


            }
        }

    }

    public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {
    }

    public void onServerEdit(ServerEditedEvent serverEditedEvent) {
    }

    public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {
    }

    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {
    }

    public void onClientMoved(ClientMovedEvent clientMovedEvent) {
    }

    public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {
    }

    public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {
    }

    public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {
    }

    public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {
    }

    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {
    }

    String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append("01ABCDWXYZabcdefghixyzFGVertutz7346tg54uzfftcw".charAt(this.rnd.nextInt("01ABCDWXYZabcdefghixyzFGVertutz7346tg54uzfftcw".length())));
        }
        if (verify.containsKey(sb.toString())) {
            return randomString(len);
        }
        return sb.toString();
    }
}

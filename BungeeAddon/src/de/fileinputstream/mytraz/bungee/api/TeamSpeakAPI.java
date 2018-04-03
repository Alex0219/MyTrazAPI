package de.fileinputstream.mytraz.bungee.api;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import de.fileinputstream.mytraz.bungee.listeners.ts3.Teamspeak3Listener;
import de.fileinputstream.mytraz.bungee.sql.MySQL;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

/**
 * User: Alexander<br/>
 * Date: 26.02.2018<br/>
 * Time: 17:22<br/>
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
public class TeamSpeakAPI {


    public static TS3Api api;
    public static TS3Query query;


    public static void connect() {
        TS3Config config = new TS3Config();
        config.setHost("mytraz.net");
        query = new TS3Query(config);
        query.connect();
        api = query.getApi();
        api.login("serveradmin", "yJvNgSYu");
        api.selectVirtualServerById(6);
        api.setNickname("TSLink");
        api.registerAllEvents();
        api.addTS3Listeners(new Teamspeak3Listener());
        System.out.println("Bot started");
    }

    public static boolean isVerified(String id) {
        MySQL.connect();
        try {
            ResultSet rs = MySQL.getResult("SELECT UUID FROM ts WHERE tsid = '" + id + "'");
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isVerified(ProxiedPlayer p) {
        MySQL.connect();
        try {
            ResultSet rs = MySQL.getResult("SELECT tsid FROM ts WHERE UUID = '" + p.getUniqueId().toString() + "'");
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getTsID(ProxiedPlayer p) {
        MySQL.connect();
        ResultSet rs = MySQL.getResult("SELECT tsid FROM ts WHERE UUID = '" + p.getUniqueId().toString() + "'");
        try {
            if (rs.next()) {
                return rs.getString("tsid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void removeVerification(ProxiedPlayer p) {
        MySQL.connect();
        String id = getTsID(p);
        if (id != null) {
            Client c = api.getClientByUId(id);
            api.removeClientFromServerGroup(34, c.getDatabaseId());
            api.sendPrivateMessage(c.getId(), "Deine Identität wurde soeben entlinkt!");
        }
        MySQL.update("DELETE FROM ts WHERE UUID = '" + p.getUniqueId().toString() + "'");
    }

    public static void notifyVerification(ProxiedPlayer p, String code) {
        MySQL.connect();
        ClientInfo client = api.getClientByUId(Teamspeak3Listener.verify.get(code));
        if (client != null) {
            api.sendPrivateMessage(client.getId(), "§7«▌§cMyTraz§7▌» Deine Identität wurde auf den Namen " + p.getName() + " gelinkt!");
            api.editClient(client.getId(), Collections.singletonMap(ClientProperty.CLIENT_DESCRIPTION, p.getName() + " | " + p.getUniqueId()));
            if (!MySQL.exists(p.getUniqueId().toString())) {
                MySQL.update("INSERT INTO ts (uuid, tsid) VALUES ('" + p.getUniqueId().toString() + "', '" + client.getUniqueIdentifier() + "')");
            } else {
                Client v = api.getClientByUId(getTsID(p));
                if (v.isInServerGroup(230)) {
                    api.removeClientFromServerGroup(230, v.getDatabaseId());
                }
                MySQL.update("UPDATE ts SET tsid = '" + client.getUniqueIdentifier() + "' WHERE UUID = '" + p.getUniqueId().toString() + "'");
            }
            api.addClientToServerGroup(230, client.getDatabaseId());
            Teamspeak3Listener.verify.remove(code);
        } else {
            p.sendMessage("§7«▌§cMyTraz§7▌» §cDu konntest nicht auf dem Teamspeak gefunden werden!");
        }
    }
}

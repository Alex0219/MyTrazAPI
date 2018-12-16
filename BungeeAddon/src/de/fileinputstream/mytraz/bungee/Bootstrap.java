package de.fileinputstream.mytraz.bungee;

import de.fileinputstream.mytraz.bungee.api.TeamSpeakAPI;
import de.fileinputstream.mytraz.bungee.commands.*;
import de.fileinputstream.mytraz.bungee.listeners.ListenerChat;
import de.fileinputstream.mytraz.bungee.listeners.ListenerLogin;
import de.fileinputstream.mytraz.bungee.manager.Files;
import de.fileinputstream.mytraz.bungee.networking.NettyServer;
import de.fileinputstream.mytraz.bungee.networking.registry.PacketRegistry;
import de.fileinputstream.mytraz.bungee.sql.MySQL;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import redis.clients.jedis.Jedis;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * User: Alexander<br/>
 * Date: 24.02.2018<br/>
 * Time: 00:35<br/>
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
public class Bootstrap extends Plugin {

    public static Bootstrap instance;
    MySQL mysql;
   ;
    Jedis jedis;
    NettyServer nettyServer;
    PacketRegistry packetRegistry;

    public static Bootstrap getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {

        instance = null;
    }

    public void scheduleUpdateTask() {
        BungeeCord.getInstance().getScheduler().schedule(getInstance(), () -> {
            if (!MySQL.isConnected()) {
                MySQL.connect();
            }
        }, 40, TimeUnit.SECONDS);
    }

    public MySQL getMysql() {
        return mysql;
    }

    @Override
    public void onEnable() {
        instance = this;
        packetRegistry = new PacketRegistry();
        buildRedis();
        mysql = new MySQL();
        TeamSpeakAPI.connect();
        nettyServer = new NettyServer("127.0.0.1",7645);
        nettyServer.run();
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new CommandHub("hub"));
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new CommandTS("ts"));
        BungeeCord.getInstance().getPluginManager().registerListener(this, new ListenerLogin());
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new Ban());
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new Unban());
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new Tempban());
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new Banlist());
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new Mutelist());
        BungeeCord.getInstance().getPluginManager().registerListener(this, new ListenerChat());
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new Mute());
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new Unmute());
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new Kick());
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new Check());
        BungeeCord.getInstance().getPluginManager().registerCommand(this, new Tempmute());
        createFolders();
        MySQL.connect();
        scheduleUpdateTask();

    }

    public void buildRedis() {
        jedis = new Jedis("127.0.0.1", 6379);
        jedis.connect();
        System.out.println("Backend -> Connected to redis.");

    }

    public void createFolders() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        Files.BanFile = new File(getDataFolder().getPath(), "bans.yml");
        if (!Files.BanFile.exists()) {
            try {
                Files.BanFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Files.MuteFile = new File(getDataFolder().getPath(), "mutes.yml");
        if (!Files.MuteFile.exists()) {
            try {
                Files.MuteFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Files.MuteConfig =
                    ConfigurationProvider.getProvider(YamlConfiguration.class).load(Files.MuteFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            Files.BanConfig =
                    ConfigurationProvider.getProvider(YamlConfiguration.class).load(Files.BanFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Jedis getJedis() {
        return jedis;
    }

    public PacketRegistry getPacketRegistry() {
        return packetRegistry;
    }
}

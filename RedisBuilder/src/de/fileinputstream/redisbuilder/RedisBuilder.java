package de.fileinputstream.redisbuilder;

import de.fileinputstream.redisbuilder.commands.CommandAddHologram;
import de.fileinputstream.redisbuilder.commands.CommandRang;
import de.fileinputstream.redisbuilder.handler.JoinHandler;
import de.fileinputstream.redisbuilder.handler.ListenerBlock;
import de.fileinputstream.redisbuilder.handler.ListenerChat;
import de.fileinputstream.redisbuilder.mod.ModdedJoinHandler;
import de.fileinputstream.redisbuilder.rank.NameTags;
import de.fileinputstream.redisbuilder.user.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

/**
 * User: Alexander<br/>
 * Date: 04.02.2018<br/>
 * Time: 17:26<br/>
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
public class RedisBuilder extends JavaPlugin {
    /**
     * Willkommen in der Hauptklasse des NyTraz SourceCodes.
     * Hier werden alle Dinge geladen, die für die Verwendung dieses Plugins notwendig sind.
     */
    public static RedisBuilder instance;
    public static WorldManager worldManager;
    Jedis jedis;


    public static RedisBuilder getInstance() {
        return instance;
    }

    public static WorldManager getWorldManager() {
        return worldManager;
    }

    @Override
    public void onDisable() {
        instance = null;
        //Disconnect from redis.
        getJedis().disconnect();
    }

    /**
     * Erzeugt die {@link Jedis} Instanz. Diese Instanzierung verbindet automatisch mit der redis Datenbank.
     */
    public void buildRedis() {
        jedis = new Jedis("127.0.0.1", 6379);
        System.out.println("Backend -> Connected to redis.");
        jedis.select(0);

    }

    /**
     * @return @{@link Jedis} Gibt die Jedis Instanz zurück.
     */
    public Jedis getJedis() {
        return jedis;
    }

    @Override
    public void onEnable() {
        instance = this;

        buildRedis();
        worldManager = new WorldManager();
        PluginManager pm = Bukkit.getPluginManager();
        //   pm.registerEvents(new JoinHandler(), this);
        pm.registerEvents(new ListenerChat(), this);
        getCommand("rang").setExecutor(new CommandRang());
        getCommand("addhologram").setExecutor(new CommandAddHologram());
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("ServerType", "Lobby");
        getConfig().addDefault("Redis-DB", 0);
        saveConfig();


        if (getConfig().getString("ServerType").equalsIgnoreCase("Unhinged")) {
            System.out.println("Backend -> Registering more listeners for modded.");
            Bukkit.getPluginManager().registerEvents(new ModdedJoinHandler(), this);
            System.out.println("Backend -> Using alternative scoreboard method.");

        } else if (getConfig().getString("ServerType").equalsIgnoreCase("Lobby")) {
            NameTags.initScoreboardTeams();
            pm.registerEvents(new JoinHandler(), this);
            pm.registerEvents(new ListenerBlock(), this);
            System.out.println("Backend -> Using normal scoreboard method.");
        }


    }

    /**
     * Checks if the normal scoreboard method is available.
     *
     * @return {@link Boolean}
     */
    public boolean scoreboardAvailable() {
        if (Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3].contains("1.8")) {
            return true;
        } else {
            if (getConfig().getString("ServerType").equalsIgnoreCase("Unhinged")) {
                return false;
            }
            return false;
        }

    }
}

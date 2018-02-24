package de.fileinputstream.mytraz.worldmanagement;

import de.fileinputstream.mytraz.worldmanagement.commands.*;
import de.fileinputstream.mytraz.worldmanagement.listeners.ListenerConnect;
import de.fileinputstream.mytraz.worldmanagement.uuid.NameTags;
import de.fileinputstream.mytraz.worldmanagement.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

/**
 * User: Alexander<br/>
 * Date: 10.02.2018<br/>
 * Time: 19:33<br/>
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
public class Bootstrap extends JavaPlugin {

    static Bootstrap instance;
    Jedis jedis;
    WorldManager worldManager;
    String spawnWorld;
    String prefix = "§7«▌§cMyTraz§7▌»";

    public static Bootstrap getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("DB", "1");
        getConfig().addDefault("SpawnWorld", "world");
        saveConfig();

        spawnWorld = getConfig().getString("SpawnWorld");
        worldManager = new WorldManager();
        jedis = new Jedis("127.0.0.1", 6379);
        jedis.connect();
        System.out.println("Connected to redis!");
        NameTags.initScoreboardTeams();

        getCommand("tpworld").setExecutor(new CommandTPWorld());
        getCommand("createworld").setExecutor(new CommandCreateWorld());
        getCommand("addresident").setExecutor(new CommandAddResident());
        getCommand("removeresident").setExecutor(new CommandRemoveResident());
        getCommand("acceptinvite").setExecutor(new CommandAcceptinvite());
        getCommand("newworldspawn").setExecutor(new CommandNewWorldSpawn());
        getCommand("worldinfo").setExecutor(new CommandWorldInfo());
        getCommand("worlds").setExecutor(new CommandWorlds());
        getCommand("setpvp").setExecutor(new CommandSetPvP());
        getCommand("tutorial").setExecutor(new CommandTutorial());
        Bukkit.getPluginManager().registerEvents(new ListenerConnect(), this);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public Jedis getJedis() {
        return jedis;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }

    public String getSpawnWorld() {
        return spawnWorld;
    }

    public String getPrefix() {
        return prefix;
    }
}
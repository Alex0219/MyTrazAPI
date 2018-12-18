package de.fileinputstream.mytraz.worldmanagement;

import de.fileinputstream.mytraz.worldmanagement.backup.BackupData;
import de.fileinputstream.mytraz.worldmanagement.backup.BackupManager;
import de.fileinputstream.mytraz.worldmanagement.chatlog.ChatLogManager;
import de.fileinputstream.mytraz.worldmanagement.commands.*;
import de.fileinputstream.mytraz.worldmanagement.listeners.ListenerChat;
import de.fileinputstream.mytraz.worldmanagement.listeners.ListenerConnect;
import de.fileinputstream.mytraz.worldmanagement.tracker.OntimeTracker;
import de.fileinputstream.mytraz.worldmanagement.uuid.NameTags;
import de.fileinputstream.mytraz.worldmanagement.world.WorldManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


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
    OntimeTracker ontimeTracker;
    ChatLogManager chatLogManager;
    BackupManager backupManager;
    String prefix = "§bFlippiGames §7»";

    public static Bootstrap getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        instance = this;


        worldManager = new WorldManager();
        backupManager = new BackupManager();
        Bukkit.getPluginManager().registerEvents(new ListenerConnect(), this);
        Bukkit.getPluginManager().registerEvents(new ListenerChat(), this);
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("DB", "1");
        getConfig().addDefault("SpawnWorld", "world");
        saveConfig();
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        jedis = new Jedis("127.0.0.1", 6379);
        jedis.connect();
        System.out.println("Connected to redis!");
        if(!getJedis().exists("survivalWorldIDCount")) {
            getJedis().set("survivalWorldIDCount","1");
            System.out.println("Backend -> Warning! Key 'survivalWorldIDCount' was missing! Created it.");
        }

        chatLogManager = new ChatLogManager();
        spawnWorld = getConfig().getString("SpawnWorld");
        ontimeTracker = new OntimeTracker();
        ontimeTracker.startCounter();
        LocalDate localDate = LocalDate.now();
        Locale spanishLocale = new Locale("de", "DE");
        String date = localDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", spanishLocale));

        Bukkit.getOnlinePlayers().forEach(p -> {
            Bukkit.getScheduler().runTaskTimer(Bootstrap.getInstance(), () -> {
                new ListenerConnect().sendTablist(p, "§l§4MyTraz §7No Limit Netzwerk\n §7- Server: §aSurvival", "§7Teamspeak: MyTraz.NET \n" + date + "\n§cSpieler online: §e" + Bukkit.getOnlinePlayers().size());
            }, 1, 1);
        });


        NameTags.initScoreboardTeams();
        loadCommands();


    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public void loadCommands() {
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
        getCommand("gm").setExecutor(new CommandGM());
        getCommand("v").setExecutor(new CommandVanish());
        getCommand("vanish").setExecutor(new CommandVanish());
        getCommand("ontime").setExecutor(new CommandOnTime());
        getCommand("warp").setExecutor(new CommandWarp());
        getCommand("setwarp").setExecutor(new CommandSetWarp());
        getCommand("delwarp").setExecutor(new CommandDelWarp());
        getCommand("rang").setExecutor(new CommandRang());
        getCommand("chatlog").setExecutor(new CommandChatLog());
        //getCommand("listbackups").setExecutor(new CommandBackupList());
        // getCommand("restorebackup").setExecutor(new CommandRestoreBackup());
        // getCommand("dobackup").setExecutor(new CommandDoBackup());
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

    public OntimeTracker getOntimeTracker() {
        return ontimeTracker;
    }

    public ChatLogManager getChatLogManager() {
        return chatLogManager;
    }

    public BackupManager getBackupManager() {
        return backupManager;
    }
}
package de.fileinputstream.none.api;


import com.blogspot.debukkitsblog.net.Datapackage;
import com.mongodb.DBCursor;
import de.fileinputstream.none.api.cache.UserCache;
import de.fileinputstream.none.api.commands.*;
import de.fileinputstream.none.api.listeners.ListenerChat;
import de.fileinputstream.none.api.listeners.ListenerCommandExecutor;
import de.fileinputstream.none.api.listeners.ListenerJoin;
import de.fileinputstream.none.api.listeners.ListenerWorldChange;
import de.fileinputstream.none.api.message.MessageManager;
import de.fileinputstream.none.api.mongo.MongoManager;
import de.fileinputstream.none.api.rank.scoreboard.NameTags;
import de.fileinputstream.none.api.resilentclient.ResilentClient;
import de.fileinputstream.none.api.sql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Bootstrap extends JavaPlugin {

    private static Bootstrap instance;

    private static MySQL mysql;

    private static MongoManager mongoManager;

    private static ResilentClient resilentClient;
    private static String serverName;
    private static UserCache userCache;
    private static MessageManager messageManager;
    public boolean isConnected;
    //Mongo

    public static String getServerName() {
        return serverName;
    }

    public static ResilentClient getResilentClient() {
        return resilentClient;
    }

    public static MessageManager getMessageManager() {
        return messageManager;
    }

    public static MySQL getMysql() {
        return mysql;
    }

    public static Bootstrap getInstance() {
        return instance;
    }

    public static MongoManager getMongoManager() {
        return mongoManager;

    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public void connectMySQL() {
        mysql = new MySQL(getConfig().getString("MySQl.Host"), getConfig().getString("MySQl.Database"), getConfig().getString("MySQl.User"), getConfig().getString("MySQl.Password"));
        mysql.connect();
        mysql.update("CREATE TABLE IF NOT EXISTS Rank (UUID VARCHAR(100), RANG VARCHAR(100))");
        mysql.update("CREATE TABLE IF NOT EXISTS History (Typ VARCHAR(100), UUID VARCHAR(100), Grund VARCHAR(100), Von VARCHAR(100), Dauer VARCHAR(100), Datum VARCHAR(100), Name VARCHAR(100))");
        mysql.update("CREATE TABLE IF NOT EXISTS Bans (Spielername VARCHAR(100), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100), Dauer VARCHAR(100), Banner VARCHAR(100) , BanID VARCHAR(100))");
        mysql.update("CREATE TABLE IF NOT EXISTS Mutes (Spielername VARCHAR(100), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100), Dauer VARCHAR(100), Muter VARCHAR(100))");
        mysql.update("CREATE TABLE IF NOT EXISTS chatlogs(id VARCHAR(10), uuid VARCHAR(64), messages LONGTEXT);");
    }

    public void connectToMongo() {
        mongoManager = new MongoManager("127.0.0.1", 27017);
        getMongoManager().connect();
    }

    @Override
    public void onEnable() {
        instance = this;
        userCache = new UserCache();
        serverName = getConfig().getString("ServerName");
        NameTags.initScoreboardTeams();
        registerCommands();
        registerListeners();
        createConfig();


        //  connectMySQL();
        connectToMongo();

        //Reload Tablist after reload


        Bukkit.getOnlinePlayers().forEach(p ->
        {
            NameTags.updateTeams();
            NameTags.addToTeam(p);
            NameTags.updateTeams();
        });


        //Connect to resilent server
        connectToResilentServer(getConfig().getString("Resilent-Host"), getConfig().getInt("Resilent-Port"));

        DBCursor cursor = getMongoManager().getPlayers().find();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }


    }

    public void registerCommands() {
        getCommand("ban").setExecutor(new CommandBan());
        getCommand("unban").setExecutor(new CommandUnBan());
        getCommand("history").setExecutor(new CommandHistory());
        getCommand("check").setExecutor(new CommandCheck());
        getCommand("mute").setExecutor(new CommandMute());
        getCommand("tempban").setExecutor(new CommandTempBan());
        getCommand("unmute").setExecutor(new CommandUnMute());
        getCommand("tempmute").setExecutor(new CommandTempMute());
        getCommand("rang").setExecutor(new CommandRang());
        getCommand("vanish").setExecutor(new ExternalCommands());
        getCommand("rang").setExecutor(new CommandRang());

    }

    public boolean isConnected() {
        return isConnected;
    }

    public void createConfig() {
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("MySQl.Host", "");
        getConfig().addDefault("MySQl.Port", 3306);
        getConfig().addDefault("MySQl.User", "root");
        getConfig().addDefault("MySQl.Database", "");
        getConfig().addDefault("MySQl.Password", "");
        getConfig().addDefault("LogMode", false);
        getConfig().addDefault("Resilent-Host", "localhost");
        getConfig().addDefault("Resilent-Port", 8000);
        getConfig().addDefault("LobbyMode", false);
        getConfig().addDefault("ServerName", "");
        getConfig().addDefault("MongoIP", "127.0.0.1");
        getConfig().addDefault("MongoPort", "27017");
        saveConfig();
    }

    public void enableSigns() {
        if (getConfig().getBoolean("LobbyMode") == true) {

        }
    }

    public void connectToResilentServer(String hostname, int port) {
        resilentClient = new ResilentClient(hostname, port);
        resilentClient.start();
        resilentClient.sendMessage(new Datapackage("HANDSHAKE", "N/Z(HU(&/GZGVT&HU&T/IJUZ/(JUIHWZ/HEUHZEHWUJDWUZDHWNJDIUWHDWNDJWDHWDN", getServerName(), Bukkit.getPort()));
    }

    public void setupInstances() {
        messageManager = new MessageManager();
    }

    public UserCache getUserCache() {
        return userCache;
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ListenerChat(), this);
        pm.registerEvents(new ListenerCommandExecutor(), this);
        //    pm.registerEvents(new ListenerLogin(), this);
        pm.registerEvents(new ListenerJoin(), this);
        pm.registerEvents(new ListenerWorldChange(), this);
        pm.registerEvents(new ExternalCommands(), this);

    }

}

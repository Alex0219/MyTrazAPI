package de.fileinputstream.mytraz.api;


import de.fileinputstream.mytraz.api.commands.*;
import de.fileinputstream.mytraz.api.listeners.ListenerChat;
import de.fileinputstream.mytraz.api.listeners.ListenerCommandExecutor;
import de.fileinputstream.mytraz.api.listeners.ListenerLogin;
import de.fileinputstream.mytraz.api.punishment.BanManager;
import de.fileinputstream.mytraz.api.punishment.HistoryManager;
import de.fileinputstream.mytraz.api.punishment.MuteManager;
import de.fileinputstream.mytraz.api.sql.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Bootstrap extends JavaPlugin {

    private static Bootstrap instance;

    private static MySQL mysql;

    private static String serverName;


    public MuteManager muteManager;

    public BanManager banManager;

    public HistoryManager historyManager;

    public static Bootstrap getInstance() {
        return instance;
    }


    @Override
    public void onDisable() {
        instance = null;
    }


    @Override
    public void onEnable() {
        instance = this;
        serverName = getConfig().getString("ServerName");
        registerCommands();
        registerListeners();
        createConfig();
        connectMySQL();
        banManager = new BanManager();
        muteManager = new MuteManager();
        historyManager = new HistoryManager();
    }

    private void registerCommands() {
        getCommand("ban").setExecutor(new CommandBan());
        getCommand("unban").setExecutor(new CommandUnBan());
        getCommand("history").setExecutor(new CommandHistory());
        getCommand("check").setExecutor(new CommandCheck());
        getCommand("mute").setExecutor(new CommandMute());
        getCommand("tempban").setExecutor(new CommandTempBan());
        getCommand("unmute").setExecutor(new CommandUnMute());
        getCommand("tempmute").setExecutor(new CommandTempMute());


    }


    private void createConfig() {
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
        saveConfig();
    }


    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ListenerChat(), this);
        pm.registerEvents(new ListenerCommandExecutor(), this);
        pm.registerEvents(new ListenerLogin(), this);
        pm.registerEvents(new ExternalCommands(), this);
    }

    public void connectMySQL() {
        mysql = new MySQL(getConfig().getString("MySQl.Host"), getConfig().getString("MySQl.Database"), getConfig().getString("MySQl.User"), getConfig().getString("MySQl.Password"));
        mysql.connect();
        mysql.update("CREATE TABLE IF NOT EXISTS Rank (UUID VARCHAR(100), RANG VARCHAR(100))");
        mysql.update("CREATE TABLE IF NOT EXISTS History (Typ VARCHAR(100), UUID VARCHAR(100), Grund VARCHAR(100), Von VARCHAR(100), Dauer VARCHAR(100), Datum VARCHAR(100), Name VARCHAR(100))");
        mysql.update("CREATE TABLE IF NOT EXISTS Bans (Spielername VARCHAR(100), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100), Dauer VARCHAR(100), Banner VARCHAR(100) , BanID VARCHAR(100))");
        mysql.update("CREATE TABLE IF NOT EXISTS Mutes (Spielername VARCHAR(100), UUID VARCHAR(100), Ende VARCHAR(100), Grund VARCHAR(100), Dauer VARCHAR(100), Muter VARCHAR(100))");
    }

    public String getServerName() {
        return serverName;
    }

    public MySQL getMysql() {
        return mysql;
    }

    public BanManager getBanManager() {
        return banManager;
    }

    public MuteManager getMuteManager() {
        return muteManager;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }
}

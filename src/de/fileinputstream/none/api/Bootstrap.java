package de.fileinputstream.none.api;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.blogspot.debukkitsblog.net.Datapackage;

import de.fileinputstream.none.api.commands.CommandBan;
import de.fileinputstream.none.api.commands.CommandCheck;
import de.fileinputstream.none.api.commands.CommandHistory;
import de.fileinputstream.none.api.commands.CommandMute;
import de.fileinputstream.none.api.commands.CommandRang;
import de.fileinputstream.none.api.commands.CommandTempBan;
import de.fileinputstream.none.api.commands.CommandTempMute;
import de.fileinputstream.none.api.commands.CommandUnBan;
import de.fileinputstream.none.api.commands.CommandUnMute;
import de.fileinputstream.none.api.listeners.ListenerChat;
import de.fileinputstream.none.api.listeners.ListenerCommandExecutor;
import de.fileinputstream.none.api.listeners.ListenerJoin;
import de.fileinputstream.none.api.listeners.ListenerLogin;
import de.fileinputstream.none.api.rank.scoreboard.NameTags;
import de.fileinputstream.none.api.resilentclient.ResilentClient;
import de.fileinputstream.none.api.sql.MySQL;

public class Bootstrap extends JavaPlugin {
	
	private static Bootstrap instance;
	
	private static MySQL mysql;
	
	private static ResilentClient resilentClient;
	
	public boolean isConnected ;
	
	private static String serverName;
	
	
	
	@Override
	public void onEnable() {
		instance = this;
		serverName = getConfig().getString("ServerName");
		NameTags.initScoreboardTeams();
		registerCommands();
		registerListeners();
		createConfig();
		connectMySQL();
		connectToResilentServer(getConfig().getString("Resilent-Host"), getConfig().getInt("Resilent-Port"));
		
		
	}
	
	@Override
	public void onDisable() {
		instance = null;
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
	  
	}
	
	public void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new ListenerChat(), this);
		pm.registerEvents(new ListenerCommandExecutor(), this);
		pm.registerEvents(new ListenerLogin(), this);
		pm.registerEvents(new ListenerJoin(), this);
	}
	
	public static MySQL getMysql() {
		return mysql;
	}
	
	public static Bootstrap getInstance() {
		return instance;
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
		saveConfig(); 
		
		}
	
	public void connectToResilentServer(String hostname, int port) {
		resilentClient = new ResilentClient(hostname, port);
		resilentClient.start();
		resilentClient.sendMessage(new Datapackage("HANDSHAKE", "N/Z(HU(&/GZGVT&HU&T/IJUZ/(JUIHWZ/HEUHZEHWUJDWUZDHWNJDIUWHDWNDJWDHWDN",getServerName()));
	}
	
	public boolean isConnected() {
		return isConnected;
	}
	
	public static ResilentClient getResilentClient() {
		return resilentClient;
	}
	
	public static String getServerName() {
		return serverName;
	}
	
	public void enableSigns() {
		if(getConfig().getBoolean("LobbyMode") == true) {
			
		}
	}
}

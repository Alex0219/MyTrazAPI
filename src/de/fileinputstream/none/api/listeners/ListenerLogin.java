package de.fileinputstream.none.api.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import de.fileinputstream.none.api.Bootstrap;
import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.punishment.BanManager;

public class ListenerLogin implements Listener {
	
	 @EventHandler
	    public void onLogin(PlayerLoginEvent e) {
	        Player p = e.getPlayer();
	        String uuid = UUIDFetcher.getUUID(p.getName()).toString();
	        if ((Bootstrap.getMysql().isConnected()) && (BanManager.isBanned(uuid))) {
	            long current = System.currentTimeMillis();
	            long end = BanManager.getEnd(uuid);
	            String Dauer = BanManager.getDauer(uuid);
	            if ((end > current) || (end == -1L)) {
	                e.disallow(PlayerLoginEvent.Result.KICK_BANNED, "§7Du wurdest §cgebannt. \n\n§aGrund : " +

	                        BanManager.getReason(uuid) +

	                        "\n§bBanID §e" + BanManager.getBanID(uuid) +

	                        "\nDu kannst im Teamspeak einen Entbannungsantrag stellen : ts.mytraz.net");
	            } else {
	                BanManager.unban(uuid);
	                e.allow();
	            }
	            if ((Bootstrap.getMysql().isConnected()) && (BanManager.isBanned(uuid))) {
	            	  e.disallow(PlayerLoginEvent.Result.KICK_BANNED, "§7Du wurdest §cgebannt. \n\n§aGrund : " +

	                        BanManager.getReason(uuid) +

	                        "\n§bBanID §e" + BanManager.getBanID(uuid) +

	                        "\nDu kannst im Teamspeak einen Entbannungsantrag stellen : ts.mytraz.net");
	            } else if(!Bootstrap.getMysql().isConnected()) {
	                e.disallow(PlayerLoginEvent.Result.KICK_BANNED,"§cEin Fehler ist aufgetreten. Bitte melden: Fehlercode: MYSQL_CON_CLOSED");
	                
	            }
	        }
	    }

	    @EventHandler
	    public void onJoin(PlayerJoinEvent e) {
	    	   String uuid = UUIDFetcher.getUUID(e.getPlayer().getName()).toString();
	        if ((!Bootstrap.getMysql().isConnected()) &&
	                (BanManager.isBanned(uuid))) {
	            BanManager.unban(uuid);
	        }    else if(!Bootstrap.getMysql().isConnected()) {
                e.getPlayer().kickPlayer("§cEin Fehler ist aufgetreten. Bitte melden: Fehlercode: MYSQL_CON_CLOSED");
                
            }
	        if(!Bootstrap.getMysql().isConnected()) {
                e.getPlayer().kickPlayer("§cEin Fehler ist aufgetreten. Bitte melden: Fehlercode: MYSQL_CON_CLOSED");
                
            }
	    }

}

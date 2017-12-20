package de.fileinputstream.none.api.listeners;

import org.bukkit.craftbukkit.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.fileinputstream.none.api.Bootstrap;
import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.punishment.MuteManager;
import de.fileinputstream.none.api.rank.RankManager;

public class ListenerChat implements Listener {
	
	@EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String msg = "%2$s";

        String uuid = UUIDFetcher.getUUID(p.getName()).toString();
        if (RankManager.getRank(uuid).equalsIgnoreCase("mod".toLowerCase())) {
            e.setFormat("§cMod §7● §c " + p.getName() + "§7 » " + msg);
        }
        if (RankManager.getRank(uuid).equalsIgnoreCase("builder".toLowerCase())) {
            e.setFormat("§eBuilder §7● §e" + p.getName() + "§7 » " + msg);
        }
        if (RankManager.getRank(uuid).equalsIgnoreCase("spieler".toLowerCase())) {
            e.setFormat("§7" + p.getName() + "§7 » " + msg);
        }
        if (RankManager.getRank(uuid).equalsIgnoreCase("dev".toLowerCase())) {
            e.setFormat("§3Dev §7● §3" + p.getName() + "§7 » " + msg);
        }
        if (RankManager.getRank(uuid).equalsIgnoreCase("premiumplus".toLowerCase())) {
            e.setFormat("§6Premium+ §7● §6" + p.getName() + "§7 » " + msg);
        }
        if (RankManager.getRank(uuid).equalsIgnoreCase("admin".toLowerCase())) {
            e.setFormat("§4Admin §7● §4" + p.getName() + "§7 » " + msg);
        }
        if (RankManager.getRank(uuid).equalsIgnoreCase("sup".toLowerCase())) {
            e.setFormat("§2Sup §7● §2" + p.getName() + "§7 » " + msg);
        }
        if (RankManager.getRank(uuid).equalsIgnoreCase("youtuber".toLowerCase())) {
            e.setFormat("§5YouTuber §7● §5" + p.getName() + "§7 » " + msg);
        }
        if (RankManager.getRank(uuid).equalsIgnoreCase("premium".toLowerCase())) {
            e.setFormat("§6" + p.getName() + "§7 » " + msg);
        }
        if (RankManager.getRank(uuid).equalsIgnoreCase("owner".toLowerCase())) {
            e.setFormat("§4Owner §7● §4" + p.getName() + "§7 » " + msg);
        }
        
    }

    @EventHandler
    public void chatFormat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String uuid = UUIDFetcher.getUUID(p.getName()).toString();
        if (Bootstrap.getMysql().isConnected() &&
                (MuteManager.isMuted(uuid))) {
            long current = System.currentTimeMillis();
            long end = MuteManager.getEnd(uuid);
            String Dauer = MuteManager.getDauer(uuid);
            if ((end == -1L | Dauer.equals("§4PERMANENT"))) {
                e.setCancelled(true);
                p.sendMessage("§cSystem §7● §7Du wurdest §4PERMANENT §7aus dem Chat gebannt§8!");
                p.sendMessage("§cSystem §7● §eMutegrund§8: §7" + MuteManager.getReason(uuid));
                return;
            }
            if (((current < end ? 1 : 0) | (end == current ? 1 : 0)) != 0) {
                e.setCancelled(true);
             
                p.sendMessage("§cSystem §7● §7Du wurdest f§r §e" + MuteManager.getDauer(uuid + " §7aus dem Chat gebannt§8!"));
                p.sendMessage("§cSystem §7● §aVerbleibende Zeit§8: §e" + MuteManager.getRemainingTime(uuid));
            } else {
                MuteManager.unmute(uuid);
                return;
            }
        }
    }

}

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

import java.util.UUID;

public class ListenerChat implements Listener {
	
	@EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String uuid = UUIDFetcher.getUUID(p.getName()).toString();
        String msg = "%2$s";

        String rank = "";
        rank = RankManager.getRank(uuid);
        Bootstrap.getInstance().getUserCache().addEntry(UUID.fromString(uuid), rank);
        if (rank.equalsIgnoreCase("mod".toLowerCase())) {
            e.setFormat("§c" + p.getName() + "§7 » " + msg);
        }
        if (rank.equalsIgnoreCase("architekt".toLowerCase())) {
            e.setFormat("§2" + p.getName() + "§7 » " + msg);
        }
        if (rank.equalsIgnoreCase("spieler".toLowerCase())) {
            e.setFormat("§7" + p.getName() + "§7 » " + msg);
        }
        if (rank.equalsIgnoreCase("dev".toLowerCase())) {
            e.setFormat("§3" + p.getName() + "§7 » " + msg);
        }
        if (rank.equalsIgnoreCase("admin".toLowerCase())) {
            e.setFormat("§4§l" + p.getName() + "§7 » " + msg);
        }
        if (rank.equalsIgnoreCase("sup".toLowerCase())) {
            e.setFormat("§1" + p.getName() + "§7 » " + msg);
        }
        if (rank.equalsIgnoreCase("youtuber".toLowerCase())) {
            e.setFormat("§5" + p.getName() + "§7 » " + msg);
        }
        if (rank.equalsIgnoreCase("premium".toLowerCase())) {
            e.setFormat("§6" + p.getName() + "§7 » " + msg);
        }
        if (rank.equalsIgnoreCase("bauleitung".toLowerCase())) {
            e.setFormat("§2§l" + p.getName() + "§7 » " + msg);
        }
        if (rank.equalsIgnoreCase("teamleitung".toLowerCase())) {
            e.setFormat("§c§l" + p.getName() + "§7 » " + msg);
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

                p.sendMessage("§cSystem §7● §7Du wurdest für §e" + MuteManager.getDauer(uuid + " §7aus dem Chat gebannt§8!"));
                p.sendMessage("§cSystem §7● §aVerbleibende Zeit§8: §e" + MuteManager.getRemainingTime(uuid));
            } else {
                MuteManager.unmute(uuid);
                return;
            }
        }
    }


}

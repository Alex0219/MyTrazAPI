package de.fileinputstream.redisbuilder.handler;


import de.fileinputstream.redisbuilder.rank.RankManager;
import de.fileinputstream.redisbuilder.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListenerChat implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String uuid = UUIDFetcher.getUUID(p.getName()).toString();
        e.setCancelled(true);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                String msg = e.getMessage();
                String rank = RankManager.getRank(uuid);
                if (rank.equalsIgnoreCase("mod")) {
                    Bukkit.broadcastMessage("§c" + p.getName() + "§7 » " + msg);
                }
                if (rank.equalsIgnoreCase("architekt")) {
                    Bukkit.broadcastMessage("§2" + p.getName() + "§7 » " + msg);
                }
                if (rank.equalsIgnoreCase("spieler")) {
                    System.out.println("ist spieler");
                    Bukkit.broadcastMessage("§7" + p.getName() + "§7 » " + msg);
                }
                if (rank.equalsIgnoreCase("dev")) {
                    Bukkit.broadcastMessage("§3" + p.getName() + "§7 » " + msg);
                }
                if (rank.equalsIgnoreCase("admin")) {
                    Bukkit.broadcastMessage("§4§l" + p.getName() + "§7 » " + msg);
                }
                if (rank.equalsIgnoreCase("sup")) {
                    Bukkit.broadcastMessage("§1" + p.getName() + "§7 » " + msg);
                }
                if (rank.equalsIgnoreCase("youtuber")) {
                    Bukkit.broadcastMessage("§5" + p.getName() + "§7 » " + msg);
                }
                if (rank.equalsIgnoreCase("premium".toLowerCase())) {
                    Bukkit.broadcastMessage("§6" + p.getName() + "§7 » " + msg);
                }
                if (rank.equalsIgnoreCase("bauleitung")) {
                    Bukkit.broadcastMessage("§2§l" + p.getName() + "§7 » " + msg);
                }
                if (rank.equalsIgnoreCase("teamleitung")) {
                    Bukkit.broadcastMessage("§c§l" + p.getName() + "§7 » " + msg);
                }

            }

        });


    }


   /*
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
    */


}

package de.fileinputstream.none.api.listeners;

import de.fileinputstream.none.api.Bootstrap;
import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.punishment.MuteManager;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

public class ListenerChat implements Listener {

    String rank = "";

  /*  @EventHandler
    public void onChat(PlayerChatEvent e) {
        Player p = e.getPlayer();
        String uuid = UUIDFetcher.getUUID(p.getName()).toString();
        e.setCancelled(true);

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                RankManager.getRank(uuid, new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        String msg = e.getMessage();
                        rank = s;
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
                        System.out.println(s);

                    }
                });
            }
        });


    }
    */



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

    @EventHandler
    public void on(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if (p.getLocation().getBlock().getType() == Material.GOLD_PLATE) {
            if (p.getLocation().subtract(0D, 2D, 0D).getBlock().getType() == Material.REDSTONE_BLOCK) {

                Vector v = p.getLocation().getDirection().multiply(17.5D).setY(4D);
                p.setVelocity(v);

                p.playEffect(p.getLocation(), Effect.ENDER_SIGNAL, 5);
                p.playSound(p.getLocation(), Sound.ENDERDRAGON_WINGS, 1F, 1F);

                p.setFallDistance(-999F);
            }
        }
    }


}

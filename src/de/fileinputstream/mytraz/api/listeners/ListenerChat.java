package de.fileinputstream.mytraz.api.listeners;

import de.fileinputstream.mytraz.api.Bootstrap;
import de.fileinputstream.mytraz.api.cache.UUIDFetcher;
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


    @EventHandler
    public void chatFormat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String uuid = UUIDFetcher.getUUID(p.getName()).toString();
        if (Bootstrap.getInstance().getMysql().isConnected() &&
                (Bootstrap.getInstance().getMuteManager().isMuted(uuid))) {
            long current = System.currentTimeMillis();
            long end = Bootstrap.getInstance().getBanManager().getEnd(uuid);
            String Dauer = Bootstrap.getInstance().getBanManager().getDauer(uuid);
            if ((end == -1L | Dauer.equals("§4PERMANENT"))) {
                e.setCancelled(true);
                p.sendMessage("§cSystem §7● §7Du wurdest §4PERMANENT §7aus dem Chat gebannt§8!");
                p.sendMessage("§cSystem §7● §eMutegrund§8: §7" + Bootstrap.getInstance().getBanManager().getReason(uuid));
                return;
            }
            if (((current < end ? 1 : 0) | (end == current ? 1 : 0)) != 0) {
                e.setCancelled(true);

                p.sendMessage("§cSystem §7● §7Du wurdest für §e" + Bootstrap.getInstance().getBanManager().getDauer(uuid + " §7aus dem Chat gebannt§8!"));
                p.sendMessage("§cSystem §7● §aVerbleibende Zeit§8: §e" + Bootstrap.getInstance().getBanManager().getRemainingTime(uuid));
            } else {
                Bootstrap.getInstance().getMuteManager().unmute(uuid);
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

package de.fileinputstream.mytraz.api.listeners;

import de.fileinputstream.mytraz.api.Bootstrap;
import de.fileinputstream.mytraz.api.cache.UUIDFetcher;
import de.fileinputstream.mytraz.api.punishment.BanManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class ListenerLogin implements Listener {

    @EventHandler
    public void onLogin(PlayerLoginEvent e) {
        Player p = e.getPlayer();
        String uuid = UUIDFetcher.getUUID(p.getName()).toString();
        if ((Bootstrap.getInstance().getMysql().isConnected()) && (Bootstrap.getInstance().getBanManager().isBanned(uuid))) {
            long current = System.currentTimeMillis();
            long end = Bootstrap.getInstance().getBanManager().getEnd(uuid);
            String Dauer = Bootstrap.getInstance().getBanManager().getDauer(uuid);
            if ((end > current) || (end == -1L)) {
                e.disallow(PlayerLoginEvent.Result.KICK_BANNED, "§7Du wurdest §cgebannt. \n\n§aGrund : " +

                        Bootstrap.getInstance().getBanManager().getReason(uuid) +

                        "\n§bBanID §e" + BanManager.getBanID(uuid) +

                        "\nDu kannst im Teamspeak einen Entbannungsantrag stellen : mytraz.net");
            } else {
                Bootstrap.getInstance().getBanManager().unban(uuid);
                e.allow();
            }
            if ((Bootstrap.getInstance().getMysql().isConnected()) && (Bootstrap.getInstance().getBanManager().isBanned(uuid))) {
                e.disallow(PlayerLoginEvent.Result.KICK_BANNED, "§7Du wurdest §cgebannt. \n\n§aGrund : " +

                        Bootstrap.getInstance().getBanManager().getReason(uuid) +

                        "\n§bBanID §e" + BanManager.getBanID(uuid) +

                        "\nDu kannst im Teamspeak einen Entbannungsantrag stellen : mytraz.net");
            } else if (!Bootstrap.getInstance().getMysql().isConnected()) {
                e.disallow(PlayerLoginEvent.Result.KICK_BANNED, "§cEin Fehler ist aufgetreten. Bitte melden: Fehlercode: MYSQL_CON_CLOSED");

            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        String uuid = UUIDFetcher.getUUID(e.getPlayer().getName()).toString();
        if ((!Bootstrap.getInstance().getMysql().isConnected()) &&
                (Bootstrap.getInstance().getBanManager().isBanned(uuid))) {
            Bootstrap.getInstance().getBanManager().unban(uuid);
        } else if (!Bootstrap.getInstance().getMysql().isConnected()) {
            e.getPlayer().kickPlayer("§cEin Fehler ist aufgetreten. Bitte melden: Fehlercode: MYSQL_CON_CLOSED");

        }
        if (!Bootstrap.getInstance().getMysql().isConnected()) {
            e.getPlayer().kickPlayer("§cEin Fehler ist aufgetreten. Bitte melden: Fehlercode: MYSQL_CON_CLOSED");

        }
    }

}

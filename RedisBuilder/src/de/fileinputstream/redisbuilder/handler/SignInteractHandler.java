package de.fileinputstream.redisbuilder.handler;

import de.fileinputstream.redisbuilder.RedisBuilder;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignInteractHandler implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractAtSign(PlayerInteractEvent event) {
        Block clickedBlock = event.getClickedBlock();
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null) {
            event.setCancelled(true);
            return;

        }
        if (!(clickedBlock instanceof Sign)) {
            event.setCancelled(true);
            return;

        }
        if (!RedisBuilder.getInstance().getNettyClient().isConnected()) {
            player.sendMessage("§cEin Fehler ist aufgetreten! Bitte melde diesen Fehler einem Teammitglied");
            event.setCancelled(true);
            return;
        }
        Sign sign = (Sign) clickedBlock;
        String signGroupLine = sign.getLine(1);
        RedisBuilder.getInstance().getServerGUI().showServersOfGroup(player, signGroupLine);
    }

    public void onSignChanged(SignChangeEvent event) {
        Sign sign = (Sign) event.getBlock();
        if(sign.getLine(0).equalsIgnoreCase("[Cloud]")) {
            String group = sign.getLine(1);
            sign.setLine(0,"§m------------------");
            sign.setLine(1,"§7>> " + group);
            sign.setLine(2,"§m------------------");
            event.getPlayer().sendMessage("§Schild wurde erstellt.");

        }
    }
}

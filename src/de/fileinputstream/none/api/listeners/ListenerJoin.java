package de.fileinputstream.none.api.listeners;

import java.lang.reflect.Field;
import java.util.UUID;

import de.fileinputstream.none.api.Bootstrap;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.rank.RankManager;
import de.fileinputstream.none.api.rank.scoreboard.NameTags;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;

public class ListenerJoin implements Listener {
	
	@EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (int i = 0; i < 150; i++) {
            player.sendMessage("");
        }
    	String uuid = UUIDFetcher.getUUID(player.getName()).toString();
        if (Bootstrap.getInstance().getUserCache().entryExists(UUID.fromString(uuid))) {
            return;
        } else {
            if (!RankManager.playerExists(uuid)) {
                RankManager.createPlayer(uuid);
                NameTags.addToTeam(player);
                NameTags.updateTeams();
                Bootstrap.getInstance().getUserCache().addEntry(UUID.fromString(uuid), "SPIELER");

            } else {
                NameTags.addToTeam(player);
                NameTags.updateTeams();
                Bootstrap.getInstance().getUserCache().addEntry(UUID.fromString(uuid), RankManager.getRank(uuid));
            }
        }

      //  event.setJoinMessage(null);


        sendTablistHeaderAndFooter(player, "§7§lMyTraz.net - §6No Limit Modded Minecraft Netzwerk", "§7Unser Teamspeak: §cts.mytraz.net");
    }

    public void sendTablistHeaderAndFooter(Player p, String header, String footer) {
        if (header == null) header = "";
        if (footer == null) footer = "";

        IChatBaseComponent tabHeader = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + header + "\"}");
        IChatBaseComponent tabFooter = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + footer + "\"}");

        PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter(tabHeader);
        try {
            Field field = headerPacket.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(headerPacket, tabFooter);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(headerPacket);
        }
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity().getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
    	if(RankManager.ranks.containsKey(UUIDFetcher.getUUID(event.getPlayer().getName()))) {
    		RankManager.ranks.remove(UUIDFetcher.getUUID(event.getPlayer().getName()));
    	}
    	
    }
   
   
}

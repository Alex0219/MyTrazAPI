package de.fileinputstream.none.api.listeners;

import de.fileinputstream.none.api.cache.UUIDFetcher;
import de.fileinputstream.none.api.handling.JoinHandler;
import de.fileinputstream.none.api.rank.RankManager;
import de.fileinputstream.none.api.rank.scoreboard.NameTags;
import de.fileinputstream.none.api.user.MyTrazUser;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.reflect.Field;

public class ListenerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        new MyTrazUser(event.getPlayer().getName()).createMyTrazUser();
        // new JoinHandler().handleJoin(new MyTrazUser(event.getPlayer().getName()));
        Player player = event.getPlayer();
        for (int i = 0; i < 150; i++) {
            player.sendMessage("");
        }

        NameTags.addToTeam(player);
        NameTags.updateTeams();

        sendTablistHeaderAndFooter(player, "§7§lMyTraz.net - §6No Limit Modded Minecraft Netzwerk", "§7Unser Teamspeak: §cts.mytraz.net");
    }
    //Set Tablist



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

    //Set Death Message 'null'
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


    @Deprecated
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (RankManager.ranks.containsKey(UUIDFetcher.getUUID(event.getPlayer().getName()))) {
            RankManager.ranks.remove(UUIDFetcher.getUUID(event.getPlayer().getName()));
        }

    }


}

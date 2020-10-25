package de.fileinputstream.mytraz.worldmanagement.npc;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.item.ItemBuilder;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import io.netty.channel.*;
import net.minecraft.server.v1_16_R2.EnumHand;
import net.minecraft.server.v1_16_R2.PacketPlayInUseEntity;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

/**
 * Created by Alexander on 15.08.2020
 * © 2020 Alexander Fiedler
 **/
public class NPCPlayerInjector implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        injectPlayer(event.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        removePlayer(event.getPlayer());
    }

    public void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove(player.getName());
        });
    }

    public void injectPlayer(Player player) {
        ChannelDuplexHandler channelDuplexHandler = new ChannelDuplexHandler() {

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {

                if (packet instanceof PacketPlayInUseEntity) {
                    PacketPlayInUseEntity use = (PacketPlayInUseEntity) packet;
                    Field f = use.getClass().getDeclaredField("a");
                    f.setAccessible(true);
                    if (use.c() == EnumHand.MAIN_HAND) {
                        if (use.b() == PacketPlayInUseEntity.EnumEntityUseAction.INTERACT) {
                            int entityId = f.getInt(use);
                            CustomNPCPlayer customNPCPlayer = Bootstrap.getInstance().getNpcManager().findCustomNPCPlayerByID(entityId);
                            if (customNPCPlayer != null) {
                                if (customNPCPlayer.getName().equalsIgnoreCase("XP-Lager")) {
                                    openEXPInventory(player);
                                }
                            }

                        }
                    }
                }
                super.channelRead(channelHandlerContext, packet);
            }

            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {

                super.write(channelHandlerContext, packet, channelPromise);
            }

        };

        ChannelPipeline pipeline = ((CraftPlayer) player).getHandle().playerConnection.networkManager.channel.pipeline();
        pipeline.addBefore("packet_handler", player.getName(), channelDuplexHandler);
    }

    public void openEXPInventory(final Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "§6XP-Lager");
        DBUser dbUser = Bootstrap.getInstance().getRankManager().getDBUser(player.getName());
        Bukkit.getScheduler().scheduleSyncDelayedTask(Bootstrap.getInstance(), () -> {
            org.bukkit.inventory.ItemStack greenPanepayin = new ItemBuilder(org.bukkit.Material.LIME_STAINED_GLASS_PANE, "EXP einlagern", null).build();
            org.bukkit.inventory.ItemStack redPanepayin = new ItemBuilder(org.bukkit.Material.RED_STAINED_GLASS_PANE, "EXP einlagern", null).build();
            org.bukkit.inventory.ItemStack greenPanepayout = new ItemBuilder(org.bukkit.Material.LIME_STAINED_GLASS_PANE, "EXP auslagern", null).build();
            org.bukkit.inventory.ItemStack redPanepayout = new ItemBuilder(org.bukkit.Material.RED_STAINED_GLASS_PANE, "EXP auslagern", null).build();
            org.bukkit.inventory.ItemStack onexp = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE, "1 EXP einlagern", null).build();
            org.bukkit.inventory.ItemStack tenxp = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE, "10 EXP einlagern", null).build();
            org.bukkit.inventory.ItemStack hundredxp = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE, "100 EXP einlagern", null).build();
            org.bukkit.inventory.ItemStack fivehundred = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE, "500 EXP einlagern", null).build();
            org.bukkit.inventory.ItemStack all = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE, "Alles einlagern", null).build();

            org.bukkit.inventory.ItemStack onexppayout = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE, "1 EXP auslagern", null).build();
            org.bukkit.inventory.ItemStack tenxppayout = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE, "10 EXP auslagern", null).build();
            org.bukkit.inventory.ItemStack hundredxppayout = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE, "100 EXP auslagern", null).build();
            org.bukkit.inventory.ItemStack fivehundredpayout = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE, "500 EXP auslagern", null).build();
            org.bukkit.inventory.ItemStack allpayout = new ItemBuilder(org.bukkit.Material.EXPERIENCE_BOTTLE, "Alles auslagern", null).build();

            ItemStack xpdisplay = new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, "Eingelagert: " + dbUser.getStoredEXP() + " EXP", null).build();

            inventory.setItem(0, greenPanepayin);
            inventory.setItem(1, greenPanepayin);
            inventory.setItem(2, onexp);
            inventory.setItem(3, tenxp);
            inventory.setItem(4, hundredxp);
            inventory.setItem(5, fivehundred);
            inventory.setItem(6, all);
            inventory.setItem(7, greenPanepayin);
            inventory.setItem(8, greenPanepayin);
            inventory.setItem(9, xpdisplay);
            inventory.setItem(10, xpdisplay);
            inventory.setItem(11, xpdisplay);
            inventory.setItem(12, xpdisplay);
            inventory.setItem(13, xpdisplay);
            inventory.setItem(14, xpdisplay);
            inventory.setItem(15, xpdisplay);
            inventory.setItem(16, xpdisplay);
            inventory.setItem(17, xpdisplay);
            inventory.setItem(18, redPanepayout);
            inventory.setItem(19, redPanepayout);
            inventory.setItem(20, onexppayout);
            inventory.setItem(21, tenxppayout);
            inventory.setItem(22, hundredxppayout);
            inventory.setItem(23, fivehundredpayout);
            inventory.setItem(24, allpayout);
            inventory.setItem(25, redPanepayout);
            inventory.setItem(26, redPanepayout);

            player.openInventory(inventory);
        });
    }


}

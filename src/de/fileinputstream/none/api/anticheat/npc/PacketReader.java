package de.fileinputstream.none.api.anticheat.npc;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.fileinputstream.none.api.Bootstrap;
import de.fileinputstream.none.api.anticheat.modules.KillAura;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_8_R3.Packet;

/**
 * Created by Alexander on 23.07.2017.
 */
public class PacketReader {

    Player player;
    Channel channel;
    static ArrayList<Player> detected;

    static {
        PacketReader.detected = new ArrayList<Player>();
    }

    public PacketReader(final Player player) {
        this.player = player;
    }

    public void inject() {
        final CraftPlayer cPlayer = (CraftPlayer)this.player;
        this.channel = cPlayer.getHandle().playerConnection.networkManager.channel;
        this.channel.pipeline().addAfter("decoder", "PacketInjector",new MessageToMessageDecoder<Packet<?>>() {
            protected void decode(final ChannelHandlerContext arg0, final Packet<?> packet, final List<Object> arg2) throws Exception {
                arg2.add(packet);
                PacketReader.this.readPacket(packet);
            }
        });
    }

    public void uninject() {
        if (this.channel.pipeline().get("PacketInjector") != null) {
            this.channel.pipeline().remove("PacketInjector");
        }
    }

    public void readPacket(final Packet<?> packet) {
        if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            final int id = (int)this.getValue(packet, "a");
            if (!PacketReader.detected.contains(this.player) && KillAura.check.containsKey(this.player)) {
                final NPC npc = KillAura.check.get(this.player);
                if (npc.getEntityID() == id && this.getValue(packet, "action").toString().equalsIgnoreCase("ATTACK")) {
                    npc.animation(this.player, 1);
                    PacketReader.detected.add(this.player);
                    Bukkit.getScheduler().runTaskLater((Plugin) Bootstrap.getInstance(), (Runnable)new Runnable() {
                        @Override
                        public void run() {
                            PacketReader.this.player.kickPlayer("§7Bitte deaktiviere §cKillAura");
                    
                        }
                    }, 5L);
                }
            }
        }
    }

    public void setValue(final Object obj, final String name, final Object value) {
        try {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        }
        catch (Exception ex) {}
    }

    public Object getValue(final Object obj, final String name) {
        try {
            final Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field.get(obj);
        }
        catch (Exception ex) {
            return null;
        }
    }
}

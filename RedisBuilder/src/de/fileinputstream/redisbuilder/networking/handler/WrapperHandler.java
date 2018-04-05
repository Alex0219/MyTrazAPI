package de.fileinputstream.redisbuilder.networking.handler;

import de.fileinputstream.mcms.master.packet.PacketCloudServerActionStop;
import de.fileinputstream.mcms.master.packet.PacketRedisUpdateServers;
import de.fileinputstream.mcms.master.packet.PacketServerLogIn;
import de.fileinputstream.redisbuilder.RedisBuilder;
import de.fileinputstream.redisbuilder.networking.Packet;
import de.fileinputstream.redisbuilder.servers.enums.ServerState;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.server.v1_12_R1.DedicatedServer;
import org.bukkit.Bukkit;

public class WrapperHandler extends SimpleChannelInboundHandler<Packet> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush(new PacketServerLogIn(System.getProperty("servername"),System.getProperty("servergroup"), Bukkit.getServer().getIp(),Bukkit.getOnlinePlayers().size(),Bukkit.getMaxPlayers(),ServerState.WAITING));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        if(packet instanceof PacketCloudServerActionStop) {
            Bukkit.shutdown();
        } else if(packet instanceof PacketRedisUpdateServers) {
            PacketRedisUpdateServers redisUpdateServers = (PacketRedisUpdateServers)packet;
            RedisBuilder.getInstance().getServerRegistry().servers = redisUpdateServers.serverList;
        }
    }
}

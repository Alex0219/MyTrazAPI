package de.fileinputstream.redisbuilder.networking.handler;

import de.fileinputstream.mcms.master.packet.PacketServerStop;
import de.fileinputstream.redisbuilder.networking.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bukkit.Bukkit;

public class WrapperHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        if(packet instanceof PacketServerStop) {
            Bukkit.shutdown();
        }
    }
}

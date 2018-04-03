package de.fileinputstream.mytraz.signaddon.network.handler;


import de.fileinputstream.mytraz.signaddon.network.packet.Packet;
import de.fileinputstream.mytraz.signaddon.network.packet.packets.PacketServerConnect;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ServerHandler extends SimpleChannelInboundHandler<Packet> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Channel active!");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext arg0, Packet packet) {


    }

    @Override
    public void channelRead(ChannelHandlerContext arg0, Object object) {
        Packet packet = (Packet) object;
        if (packet instanceof PacketServerConnect) {

        }

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

    }

}




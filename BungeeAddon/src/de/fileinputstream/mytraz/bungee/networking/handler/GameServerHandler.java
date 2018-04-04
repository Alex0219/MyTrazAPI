package de.fileinputstream.mytraz.bungee.networking.handler;

import de.fileinputstream.mytraz.bungee.networking.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GameServerHandler extends SimpleChannelInboundHandler<Packet> {

    /**
     * Diese Klasse fängt alle Packete der Gameserver ab.
     * @param channelHandlerContext
     * @param packet
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {

    }
}

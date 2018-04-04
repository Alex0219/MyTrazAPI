package de.fileinputstream.redisbuilder.networking.handler;

import de.fileinputstream.redisbuilder.RedisBuilder;
import de.fileinputstream.redisbuilder.networking.Packet;
import de.fileinputstream.redisbuilder.networking.packets.PacketServerLogIn;
import de.fileinputstream.redisbuilder.servers.enums.ServerState;
import de.fileinputstream.redisbuilder.servers.server.Server;
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
        if(packet instanceof PacketServerLogIn) {
            //Cast Packet superclass to real class.
            PacketServerLogIn packetServerLogIn = (PacketServerLogIn)packet;
            String serverNane = packetServerLogIn.getName();
            String group = packetServerLogIn.getGroup();
            String ip = packetServerLogIn.getIp();
            int online = packetServerLogIn.getOnline();
            int maxOnline = packetServerLogIn.getMaxOnline();
            ServerState serverState = packetServerLogIn.getServerState();
            Server server =  new Server(serverNane,group,ip,online,maxOnline,serverState);
            if(!RedisBuilder.getInstance().getServerRegistry().servers.contains(server)) {
                RedisBuilder.getInstance().getServerRegistry().addServer(server);
                System.out.println("Backend -> Server" + server.getName() + " has been added.");
            }
        }
    }
}

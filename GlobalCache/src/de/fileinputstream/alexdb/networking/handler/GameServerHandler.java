package de.fileinputstream.alexdb.networking.handler;

import de.fileinpustream.mcms.master.packet.PacketCreateServer;
import de.fileinputstream.mcms.wrapper.Wrapper;
import de.fileinputstream.mcms.wrapper.gameserver.CloudServer;
import de.fileinputstream.mcms.wrapper.network.packet.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GameServerHandler extends SimpleChannelInboundHandler<Packet> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
        Channel channel = ctx.channel();

        if (packet instanceof PacketCreateServer) {

            PacketCreateServer serverCreate = (PacketCreateServer) packet;
            String name = serverCreate.getName();
            int ram = serverCreate.getRam();
            int slots = serverCreate.getSlots();
            String group = serverCreate.getGroup();
            String host = serverCreate.getHost();
            int port = serverCreate.getPort();
            System.out.println("Got the information from master to start a server:" + name);
            CloudServer server = new CloudServer(name, port, ram, group, slots, host);
            if (!Wrapper.getInstance().getGameServerManager().containsServer(server)) {
                server.start();
            } else {
                System.out.println("Server is already registered. Ignoring that packet!");
                return;
            }
            Wrapper.getInstance().getGameServerManager().addCloudServer(server);
        }

    }

}

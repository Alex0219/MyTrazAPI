package de.fileinputstream.alexdb.networking.handler;

import de.fileinpustream.mcms.master.packet.PacketCreateServer;
import de.fileinpustream.mcms.master.packet.PacketWrapperConnect;
import de.fileinpustream.mcms.master.packet.PacketWrapperStop;
import de.fileinputstream.mcms.wrapper.Wrapper;
import de.fileinputstream.mcms.wrapper.network.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MasterHandler extends SimpleChannelInboundHandler<Packet> {


    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("Connected to master!");
        ctx.writeAndFlush(new PacketWrapperConnect(Wrapper.getInstance().wrapperName, Wrapper.getInstance().getKey()));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet packet) {
        if (packet instanceof PacketWrapperStop) {
            System.out.println("Wrapper has been forced to stop!");
            System.exit(1);
        } else if (packet instanceof PacketCreateServer) {
            PacketCreateServer serverCreate = (PacketCreateServer) packet;
            String name = serverCreate.getName();
            String group = serverCreate.getGroup();
            int ram = serverCreate.getRam();

        }

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) {
        System.out.println("Lost connection to master server.. Retrying");


    }
}

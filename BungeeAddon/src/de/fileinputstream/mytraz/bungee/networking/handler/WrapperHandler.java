package de.fileinputstream.mytraz.bungee.networking.handler;

import de.fileinputstream.mcms.master.packet.PacketProxyServerRegister;
import de.fileinputstream.mcms.master.packet.PacketProxyServerUnregister;
import de.fileinputstream.mytraz.bungee.networking.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;

import java.net.InetSocketAddress;

public class WrapperHandler extends SimpleChannelInboundHandler<Packet> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) throws Exception {
        if(packet instanceof PacketProxyServerRegister) {
            PacketProxyServerRegister proxyServerRegister = (PacketProxyServerRegister)packet;
            String name = proxyServerRegister.getServerName();
            String ip = proxyServerRegister.getIp();
            int port = proxyServerRegister.getPort();
            ServerInfo si = BungeeCord.getInstance().constructServerInfo(name, new InetSocketAddress(ip,port), "",false);
            if(!BungeeCord.getInstance().getServers().containsKey(name)) {
                BungeeCord.getInstance().getServers().put(name,si);
                System.out.println("Server: " + name + " has been added to the proxy.");
            }
        } else if(packet instanceof PacketProxyServerUnregister) {
            PacketProxyServerUnregister proxyServerRegister = (PacketProxyServerUnregister)packet;
            String name = proxyServerRegister.getServerName();
            String ip = proxyServerRegister.getIp();
            int port = proxyServerRegister.getPort();
            ServerInfo si = BungeeCord.getInstance().constructServerInfo(name, new InetSocketAddress(ip,port), "",false);
            if(BungeeCord.getInstance().getServers().containsKey(name)) {
                BungeeCord.getInstance().getServers().remove(name);
                System.out.println("Server: " + name + " has been removed from the proxy.");
            }
        }
    }
}

package de.fileinputstream.mcms.master.packet;


import de.fileinputstream.mytraz.bungee.networking.Packet;
import de.fileinputstream.mytraz.bungee.networking.PacketSerializer;

public class PacketProxyServerRegister extends Packet {


    String serverName;
    String ip;
    int port;

    public PacketProxyServerRegister() {

    }

    public PacketProxyServerRegister(String serverName, String ip, int port) {
        this.serverName = serverName;
        this.ip = ip;
        this.port = port;
    }
    @Override
    public void readPacket(PacketSerializer serializer) {
        this.serverName = serializer.readString();
    }

    @Override
    public void writePacket(PacketSerializer serializer) {
        serializer.writeString(serverName);
    }

    public String getServerName() {
        return serverName;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}

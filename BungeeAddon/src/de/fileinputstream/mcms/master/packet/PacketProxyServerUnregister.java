package de.fileinputstream.mcms.master.packet;


import de.fileinputstream.mytraz.bungee.networking.Packet;
import de.fileinputstream.mytraz.bungee.networking.PacketSerializer;

public class PacketProxyServerUnregister extends Packet {

    String serverName;
    String ip;
    int port;

    public PacketProxyServerUnregister() {

    }

    public PacketProxyServerUnregister(String serverName, String ip, int port) {
        this.serverName = serverName;
        this.ip = ip;
        this.port = port;
    }

    public PacketProxyServerUnregister(String serverName) {
        this.serverName = serverName;
    }
    @Override
    public void readPacket(PacketSerializer serializer) {
        this.serverName = serializer.readString();
        this.ip = serializer.readString();
        this.port = serializer.readInt();
    }

    @Override
    public void writePacket(PacketSerializer serializer) {
        serializer.writeString(serverName);
        serializer.writeString(ip);
        serializer.writeInt(port);
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

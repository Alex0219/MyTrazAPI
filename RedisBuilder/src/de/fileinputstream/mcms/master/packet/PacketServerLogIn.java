package de.fileinputstream.mcms.master.packet;

import de.fileinputstream.redisbuilder.networking.Packet;
import de.fileinputstream.redisbuilder.networking.PacketSerializer;
import de.fileinputstream.redisbuilder.servers.enums.ServerState;

public class PacketServerLogIn extends Packet {

    String name;
    String group;
    String ip;
    int online;
    int maxOnline;
    ServerState serverState;

    public PacketServerLogIn() {

    }

    public PacketServerLogIn(String name, String group, String ip, int online, int maxOnline, ServerState serverState) {
        this.name = name;
        this.group = group;
        this.ip = ip;
        this.online = online;
        this.maxOnline = maxOnline;
        this.serverState = serverState;
    }

    @Override
    public void readPacket(PacketSerializer serializer) {
        serializer.writeString(name);
        serializer.writeString(group);
        serializer.writeString(ip);
        serializer.writeInt(online);
        serializer.writeInt(maxOnline);
        serializer.writeObject(serverState);

    }

    @Override
    public void writePacket(PacketSerializer serializer) {
        this.name = serializer.readString();
        this.group = serializer.readString();
        this.ip = serializer.readString();
        this.online = serializer.readInt();
        this.maxOnline = serializer.readInt();
        this.serverState = (ServerState) serializer.readObject();
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getIp() {
        return ip;
    }

    public int getOnline() {
        return online;
    }

    public int getMaxOnline() {
        return maxOnline;
    }

    public ServerState getServerState() {
        return serverState;
    }
}

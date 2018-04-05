package de.fileinputstream.mcms.master.packet;


import de.fileinputstream.redisbuilder.networking.Packet;
import de.fileinputstream.redisbuilder.networking.PacketSerializer;
import de.fileinputstream.redisbuilder.servers.enums.ServerState;

public class PacketUpdateServerData extends Packet {

    String serverName;

    int online;
    int maxOnline;
    ServerState state;

    public PacketUpdateServerData(String serverName, int online, int maxPlayers, ServerState state) {
        this.serverName = serverName;
        this.online = online;
        this.maxOnline = maxPlayers;
        this.state = state;
    }

    @Override
    public void readPacket(PacketSerializer serializer) {
        this.serverName = serializer.readString();
        this.online = serializer.readInt();
        this.state = (ServerState) serializer.readObject();
    }

    @Override
    public void writePacket(PacketSerializer serializer) {
        serializer.writeString(serverName);
        serializer.writeInt(online);
        serializer.writeInt(maxOnline);
        serializer.writeObject(state);
    }
}

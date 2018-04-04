package de.fileinputstream.mcms.master.packet;

import de.fileinputstream.redisbuilder.networking.Packet;
import de.fileinputstream.redisbuilder.networking.PacketSerializer;
import de.fileinputstream.redisbuilder.servers.server.Server;

import java.util.ArrayList;

public class PacketRedisUpdateServers extends Packet {


    ArrayList<Server> serverList = new ArrayList<>();

    public PacketRedisUpdateServers() {

    }

    public PacketRedisUpdateServers(ArrayList<Server> serverList) {
        this.serverList = serverList;
    }

    @Override
    public void readPacket(PacketSerializer serializer) {
        this.serverList = (ArrayList<Server>) serializer.readObject();
    }

    @Override
    public void writePacket(PacketSerializer serializer) {
        serializer.writeObject(serverList);
    }
}

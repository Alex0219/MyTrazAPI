package de.fileinputstream.redisbuilder.networking.packets;

import de.fileinputstream.redisbuilder.networking.Packet;
import de.fileinputstream.redisbuilder.networking.PacketSerializer;

public class PacketServerLogIn extends Packet {

    String name;
    String group;

    public PacketServerLogIn() {

    }

    public PacketServerLogIn(String name, String group) {
        this.name = name;
        this.group = group;
    }

    @Override
    public void readPacket(PacketSerializer serializer) {
        serializer.writeString(name);
        serializer.writeString(group);
    }

    @Override
    public void writePacket(PacketSerializer serializer) {
        this.name = serializer.readString();
        this.group = serializer.readString();
    }
}

package de.fileinputstream.redisbuilder.networking;

public abstract class Packet {

    public abstract void readPacket(PacketSerializer serializer);

    public abstract void writePacket(PacketSerializer serializer);
}

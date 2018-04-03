package de.fileinputstream.mytraz.signaddon.network.packet;


import de.fileinputstream.mytraz.signaddon.network.serializer.PacketSerializer;

public abstract class Packet {

    public abstract void readPacket(PacketSerializer serializer);

    public abstract void writePacket(PacketSerializer serializer);

}

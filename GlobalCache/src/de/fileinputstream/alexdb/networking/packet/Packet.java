package de.fileinputstream.alexdb.networking.packet;

import de.fileinputstream.mcms.wrapper.network.serializer.PacketSerializer;

public abstract class Packet {

    public abstract void readPacket(PacketSerializer serializer);

    public abstract void writePacket(PacketSerializer serializer);

}

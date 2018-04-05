package de.fileinputstream.mcms.master.packet;


import de.fileinputstream.redisbuilder.networking.Packet;
import de.fileinputstream.redisbuilder.networking.PacketSerializer;

public class PacketCloudServerDisconnect extends Packet {

    public PacketCloudServerDisconnect(String cause) {

    }

    public PacketCloudServerDisconnect() {

    }

    String cause;
    @Override
    public void readPacket(PacketSerializer serializer) {
        this.cause = serializer.readString();
    }

    @Override
    public void writePacket(PacketSerializer serializer) {
        serializer.writeString(cause);
    }
}

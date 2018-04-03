package de.fileinputstream.mytraz.signaddon.network.cypt;

import de.fileinputstream.mcms.master.MCMS;
import de.fileinputstream.mcms.master.network.packet.Packet;
import de.fileinputstream.mcms.master.network.serializer.PacketSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext arg0, Packet packet, ByteBuf arg2) throws Exception {
        if (arg2 instanceof EmptyByteBuf) {
            return;
        }
        PacketSerializer serializer = new PacketSerializer(arg2);

        int packetId = MCMS.getInstance().getPacketRegistry().getIDByPacketClass(packet.getClass());
        if (packetId == 0)
            throw new Exception("Unknown Packet " + packet.getClass().getName());

        serializer.writeInt(packetId);

        packet.writePacket(serializer);

    }

}

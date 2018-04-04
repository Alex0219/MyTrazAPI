package de.fileinputstream.mytraz.bungee.networking.codec;


import de.fileinputstream.mytraz.bungee.Bootstrap;
import de.fileinputstream.mytraz.bungee.networking.Packet;
import de.fileinputstream.mytraz.bungee.networking.PacketSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

    /**
     * Hier wird das Paket codiert und dann an durch den Channel geschoben.
     * Wenn das {@link ByteBuf} Objekt leer ist, wird nichts getan.
     * @param arg0
     * @param packet
     * @param arg2
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext arg0, Packet packet, ByteBuf arg2) throws Exception {
        if(arg2 instanceof EmptyByteBuf) {
            return;
        }
        PacketSerializer serializer = new PacketSerializer(arg2);
        int packetId = Bootstrap.getInstance().getPacketRegistry().getIDByPacketClass(packet.getClass());
        if (packetId == 0) {
            throw new Exception("Unknown Packet " + packet.getClass().getName());
        } else {
            serializer.writeInt(packetId);
            packet.writePacket(serializer);
        }


    }
}

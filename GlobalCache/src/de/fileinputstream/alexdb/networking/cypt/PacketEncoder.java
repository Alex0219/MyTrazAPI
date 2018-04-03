package de.fileinputstream.alexdb.networking.cypt;


import de.fileinputstream.alexdb.networking.packet.Packet;
import de.fileinputstream.alexdb.networking.serializer.PacketSerializer;
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

        int packetId = 0;//AlexDB.getInstance().getPacketRegistry().getIDByPacketClass(packet.getClass());
        if (packetId == 0) {
            throw new Exception("Unknown Packet " + packet.getClass().getName());
        } else {
            serializer.writeInt(packetId);

            System.out.println("Ok");
            packet.writePacket(serializer);
        }


    }

}

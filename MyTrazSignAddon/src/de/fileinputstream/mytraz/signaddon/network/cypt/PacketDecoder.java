package de.fileinputstream.mytraz.signaddon.network.cypt;

import de.fileinputstream.mcms.master.MCMS;
import de.fileinputstream.mcms.master.network.packet.Packet;
import de.fileinputstream.mcms.master.network.serializer.PacketSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) {

        try {
            if (byteBuf instanceof EmptyByteBuf) {
                return;
            }
            PacketSerializer packetBuffer = new PacketSerializer(byteBuf);
            int packetId = packetBuffer.readInt();

            Class<? extends Packet> packet = MCMS.getInstance().getPacketRegistry().getPacketClassByID(packetId);

            if (packet == null) {
                throw new Exception("Unknown packet id:" + packetId);
            } else {
                Packet nettyPacket = packet.newInstance();
                nettyPacket.readPacket(packetBuffer);
                list.add(nettyPacket);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}

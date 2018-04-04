package de.fileinputstream.mytraz.bungee.networking.codec;

import de.fileinputstream.mytraz.bungee.Bootstrap;
import de.fileinputstream.mytraz.bungee.networking.Packet;
import de.fileinputstream.mytraz.bungee.networking.PacketSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {

        try {
            if(byteBuf instanceof EmptyByteBuf) {
                return;
            }
            PacketSerializer packetBuffer = new PacketSerializer(byteBuf);
            int packetId = packetBuffer.readInt();

            Class<? extends Packet> packet = Bootstrap.getInstance().getPacketRegistry().getPacketClassByID(packetId);

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

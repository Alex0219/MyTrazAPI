package de.fileinputstream.mytraz.signaddon.network.packet.registry;

import de.fileinputstream.mytraz.signaddon.network.packet.Packet;

import java.util.HashMap;
import java.util.Map;


public class PacketRegistry {

    private Map<Integer, Packet> packets = new HashMap<>();

    public PacketRegistry() {

    }

    public Class<? extends Packet> getPacketClassByID(int id) {
        Packet packet = packets.get(id);
        if (packet == null) {
            return null;
        }
        return packet.getClass();
    }

    public int getIDByPacketClass(Class<? extends Packet> clazz) {
        for (Map.Entry<Integer, Packet> entry : packets.entrySet())
            if (entry.getValue().getClass() == clazz) {
                return entry.getKey();
            }
        return 0;
    }

}

package de.fileinputstream.mytraz.bungee.networking.registry;



import de.fileinputstream.mcms.master.packet.PacketProxyServerRegister;
import de.fileinputstream.mcms.master.packet.PacketProxyServerUnregister;
import de.fileinputstream.mytraz.bungee.networking.Packet;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {

    private Map<Integer, Packet> packets = new HashMap<>();

    public PacketRegistry() {
        packets.put(1, new PacketProxyServerRegister());
        packets.put(2, new PacketProxyServerUnregister());
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

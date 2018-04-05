package de.fileinputstream.redisbuilder.networking.registry;

import de.fileinputstream.mcms.master.packet.PacketCloudServerActionStop;
import de.fileinputstream.mcms.master.packet.PacketCloudServerDisconnect;
import de.fileinputstream.mcms.master.packet.PacketRedisUpdateServers;
import de.fileinputstream.redisbuilder.networking.Packet;
import de.fileinputstream.mcms.master.packet.PacketServerLogIn;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {

    private Map<Integer, Packet> packets = new HashMap<>();

    public PacketRegistry() {
        packets.put(12, new PacketServerLogIn());
        packets.put(5, new PacketCloudServerActionStop());
        packets.put(9, new PacketRedisUpdateServers());
        packets.put(6, new PacketCloudServerDisconnect());

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

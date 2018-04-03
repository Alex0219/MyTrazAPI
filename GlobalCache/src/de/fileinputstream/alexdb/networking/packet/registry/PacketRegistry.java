package de.fileinputstream.alexdb.networking.packet.registry;

import de.fileinpustream.mcms.master.packet.PacketCloudServerConnect;
import de.fileinpustream.mcms.master.packet.PacketCreateServer;
import de.fileinpustream.mcms.master.packet.PacketWrapperConnect;
import de.fileinpustream.mcms.master.packet.PacketWrapperStop;
import de.fileinputstream.mcms.wrapper.network.packet.Packet;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


public class PacketRegistry {

    private Map<Integer, Packet> packets = new HashMap<>();

    public PacketRegistry() {
        packets.put(1, new PacketWrapperConnect());
        packets.put(2, new PacketCreateServer());
        packets.put(3, new PacketCloudServerConnect());
        packets.put(4, new PacketWrapperStop());
    }

    public Class<? extends Packet> getPacketClassByID(int id) {
        Packet packet = packets.get(id);
        if (packet == null) {
            return null;
        }
        return packet.getClass();
    }

    public int getIDByPacketClass(Class<? extends Packet> clazz) {
        for (Entry<Integer, Packet> entry : packets.entrySet())
            if (entry.getValue().getClass() == clazz) {
                return entry.getKey();
            }
        return 0;
    }

}

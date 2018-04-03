package de.fileinputstream.redisbuilder.servers;

import de.fileinputstream.redisbuilder.RedisBuilder;
import de.fileinputstream.redisbuilder.servers.server.Server;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ServerGUI {

    /**
     * Ist für das Darstellen der Server im Spielerinventar da.
     */
    /*
    Zeigt dem Spieler alle Server einer bestimmten Gruppe an.
     */
    public void showServersOfGroup(Player player, String name) {
        Inventory inv = Bukkit.createInventory(null, 9 * 4, "Wähle einen §c" + name + "§6Server");

        for (Server servers : RedisBuilder.getInstance().getServerRegistry().servers) {
            if (servers.getGroup().equalsIgnoreCase(name)) {
                ItemStack server = new ItemStack(Material.STAINED_CLAY, (byte) 6);
                ItemMeta meta = server.getItemMeta();
                meta.setDisplayName("§7Join >> §c" + servers.getName());
                List<String> lores = new ArrayList<>();
                lores.add("§m---------");
                lores.add("§7Online: " + servers.getOnline() + "/" + servers.getMaxOnline());
                lores.add("§7Status:" + servers.getServerState());
                lores.add("§m---------");
                meta.setLore(lores);
                server.setItemMeta(meta);
                inv.addItem(server);
                player.openInventory(inv);


            }

        }

    }
}

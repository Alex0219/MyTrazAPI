package de.fileinputstream.redisbuilder.servers;

import de.fileinputstream.redisbuilder.RedisBuilder;
import de.fileinputstream.redisbuilder.servers.enums.ServerState;
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
     *
    /*
    Zeigt dem Spieler alle Server einer bestimmten Gruppe an.
     */
    public void showServersOfGroup(final Player player, final String name) {
       final Inventory inv = Bukkit.createInventory(null, 9 * 4, "Wähle einen §c" + name + "§6Server");

        for (Server servers : RedisBuilder.getInstance().getServerRegistry().servers) {
            if (servers.getGroup().equalsIgnoreCase(name)) {
                if(servers.getOnline() == servers.getMaxOnline() ) {
                    final ItemStack server = new ItemStack(Material.STAINED_CLAY, (byte) 4);
                    final  ItemMeta meta = server.getItemMeta();
                    meta.setDisplayName("§7Join >> §c" + servers.getName());
                    final List<String> lores = new ArrayList<>();
                    lores.add("§m---------");
                    lores.add("§7Online: " + servers.getOnline() + "/" + servers.getMaxOnline());
                    lores.add("§7Status:" + servers.getServerState());
                    lores.add("§m---------");
                    meta.setLore(lores);
                    server.setItemMeta(meta);
                    inv.addItem(server);
                    player.openInventory(inv);
                }
                final ItemStack server = new ItemStack(Material.STAINED_CLAY, (byte) 5);
              final  ItemMeta meta = server.getItemMeta();
                meta.setDisplayName("§7Join >> §c" + servers.getName());
                final List<String> lores = new ArrayList<>();
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

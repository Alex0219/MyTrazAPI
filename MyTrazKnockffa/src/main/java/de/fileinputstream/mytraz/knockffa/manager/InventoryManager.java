package de.fileinputstream.mytraz.knockffa.manager;

import de.fileinputstream.mytraz.knockffa.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryManager {

    public void buildStatsInventory(GamePlayer player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.ENDER_CHEST, "Stats von:" + player.getName());


    }
}

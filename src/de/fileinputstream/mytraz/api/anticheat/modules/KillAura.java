package de.fileinputstream.mytraz.api.anticheat.modules;


import de.fileinputstream.mytraz.api.Bootstrap;
import de.fileinputstream.mytraz.api.anticheat.npc.NPC;
import de.fileinputstream.mytraz.api.anticheat.npc.PacketReader;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Alexander on 23.07.2017.
 */
public class KillAura implements Listener {

    public static ArrayList<Player> checked;
    public static HashMap<Player, NPC> check;
    public static HashMap<Player, PacketReader> packets;

    static {
        KillAura.checked = new ArrayList<Player>();
        KillAura.check = new HashMap<Player, NPC>();
        KillAura.packets = new HashMap<Player, PacketReader>();
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final PacketReader reader = new PacketReader(player);
        KillAura.packets.put(player, reader);
        reader.inject();
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        if (KillAura.packets.containsKey(player)) {
            final PacketReader reader = KillAura.packets.get(player);
            reader.uninject();
        }
    }

    public static void performCheck(final Player player) {
        if (KillAura.check.get(player) == null) {
            spawn(player);
            Bukkit.getScheduler().runTaskLaterAsynchronously(Bootstrap.getInstance(), new Runnable() {
                @Override
                public void run() {
                    KillAura.despawn(player);
                }
            }, 12L);
        }
    }

    @EventHandler
    public void onMove(final PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location Above = player.getLocation().add(player.getLocation().getDirection().setY(-3).normalize().multiply(-3));
        final Location Behind = player.getLocation().add(player.getLocation().getDirection().setY(0).normalize().multiply(-3));
        if (KillAura.check.get(player) != null) {
            if (player.getLocation().getPitch() <= -30.0f) {
                KillAura.check.get(player).teleport(player, Behind);
            }
            else {
                KillAura.check.get(player).teleport(player, Above);
            }
        }
    }

    public static ItemStack getRandomHelmet() {
        ItemStack item = null;
        final Random r = new Random();
        final int zufall = r.nextInt(4);
        switch (zufall) {
            case 0: {
                item = new ItemStack(Items.LEATHER_HELMET);
                break;
            }
            case 1: {
                item = new ItemStack(Items.CHAINMAIL_HELMET);
                break;
            }
            case 2: {
                item = new ItemStack(Items.GOLDEN_HELMET);
                break;
            }
            case 3: {
                item = new ItemStack(Items.IRON_HELMET);
                break;
            }
            case 4: {
                item = new ItemStack(Items.DIAMOND_HELMET);
                break;
            }
        }
        return item;
    }

    public static void spawn(final Player player) {
        final Location Above = player.getLocation().add(player.getLocation().getDirection().setY(-12).normalize().multiply(-12));
        final Location Behind = player.getLocation().add(player.getLocation().getDirection().setY(0).normalize().multiply(-12));
        if (player.getLocation().getPitch() <= -30.0f) {
            final NPC npc = new NPC(getrandomName(), Behind);
            npc.spawn(player);
            npc.equip(player, 4, getRandomHelmet());
            npc.equip(player, 3, getRandomChestPlate());
            npc.equip(player, 2, getRandomLeggings());
            npc.equip(player, 1, getRandomBoots());
            npc.equip(player, 0, getRandomSword());
            npc.rmvFromTablist(player);
            KillAura.check.put(player, npc);
        }
        else {
            final NPC npc = new NPC(getrandomName(), Above);
            npc.spawn(player);
            npc.equip(player, 4, getRandomHelmet());
            npc.equip(player, 3, getRandomChestPlate());
            npc.equip(player, 2, getRandomLeggings());
            npc.equip(player, 1, getRandomBoots());
            npc.equip(player, 0, getRandomSword());
            npc.rmvFromTablist(player);
            KillAura.check.put(player, npc);
        }
    }

    public static void despawn(final Player player) {
        if (KillAura.check.containsKey(player)) {
            final NPC npc = KillAura.check.get(player);
            npc.destroy(player);
            KillAura.check.remove(player);
        }
    }

    public static String getrandomName() {
        final List<String> players = new ArrayList<String>();
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            players.add(pl.getName());
        }
        final Random r = new Random();
        return players.get(r.nextInt(players.size()));
    }

    public static ItemStack getRandomSword() {
        ItemStack item = null;
        final Random r = new Random();
        final int zufall = r.nextInt(4);
        switch (zufall) {
            case 0: {
                item = new ItemStack(Items.WOODEN_SWORD);
                break;
            }
            case 1: {
                item = new ItemStack(Items.STONE_SWORD);
                break;
            }
            case 2: {
                item = new ItemStack(Items.GOLDEN_SWORD);
                break;
            }
            case 3: {
                item = new ItemStack(Items.IRON_SWORD);
                break;
            }
            case 4: {
                item = new ItemStack(Items.DIAMOND_SWORD);
                break;
            }
        }
        return item;
    }

    public static ItemStack getRandomChestPlate() {
        ItemStack item = null;
        final Random r = new Random();
        final int zufall = r.nextInt(4);
        switch (zufall) {
            case 0: {
                item = new ItemStack(Items.LEATHER_CHESTPLATE);
                break;
            }
            case 1: {
                item = new ItemStack(Items.CHAINMAIL_CHESTPLATE);
                break;
            }
            case 2: {
                item = new ItemStack(Items.GOLDEN_CHESTPLATE);
                break;
            }
            case 3: {
                item = new ItemStack(Items.IRON_CHESTPLATE);
                break;
            }
            case 4: {
                item = new ItemStack(Items.DIAMOND_CHESTPLATE);
                break;
            }
        }
        return item;
    }

    public static ItemStack getRandomLeggings() {
        ItemStack item = null;
        final Random r = new Random();
        final int zufall = r.nextInt(4);
        switch (zufall) {
            case 0: {
                item = new ItemStack(Items.LEATHER_LEGGINGS);
                break;
            }
            case 1: {
                item = new ItemStack(Items.CHAINMAIL_LEGGINGS);
                break;
            }
            case 2: {
                item = new ItemStack(Items.GOLDEN_LEGGINGS);
                break;
            }
            case 3: {
                item = new ItemStack(Items.IRON_LEGGINGS);
                break;
            }
            case 4: {
                item = new ItemStack(Items.DIAMOND_LEGGINGS);
                break;
            }
        }
        return item;
    }

    public static ItemStack getRandomBoots() {
        ItemStack item = null;
        final Random r = new Random();
        final int zufall = r.nextInt(4);
        switch (zufall) {
            case 0: {
                item = new ItemStack(Items.LEATHER_BOOTS);
                break;
            }
            case 1: {
                item = new ItemStack(Items.CHAINMAIL_BOOTS);
                break;
            }
            case 2: {
                item = new ItemStack(Items.GOLDEN_BOOTS);
                break;
            }
            case 3: {
                item = new ItemStack(Items.IRON_BOOTS);
                break;
            }
            case 4: {
                item = new ItemStack(Items.DIAMOND_BOOTS);
                break;
            }
        }
        return item;
    }

    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            final Player player = (Player) event.getDamager();
            if (player.getGameMode().equals(GameMode.CREATIVE)) {
                return;
            }
            if (KillAura.check.containsKey(player)) {
                final NPC npc = KillAura.check.get(player);
                npc.animation(player, 0);
            }
            if (!KillAura.checked.contains(player)) {
                KillAura.checked.add(player);
                performCheck(player);
                Bukkit.getScheduler().runTaskLaterAsynchronously(Bootstrap.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        KillAura.checked.remove(player);
                    }
                }, 200L);
            }
        }
    }
}

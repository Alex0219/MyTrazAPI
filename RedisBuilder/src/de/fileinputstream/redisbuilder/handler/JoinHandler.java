package de.fileinputstream.redisbuilder.handler;

import de.fileinputstream.redisbuilder.RedisBuilder;
import de.fileinputstream.redisbuilder.user.DBUser;
import de.fileinputstream.redisbuilder.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * User: Alexander<br/>
 * Date: 04.02.2018<br/>
 * Time: 19:07<br/>
 * MIT License
 * <p>
 * Copyright (c) 2017 Alexander Fiedler
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use and modify without distributing the software to anybody else,
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p>
 * <p>
 * MIT Lizenz
 * Copyright (c) 2017 Alexander Fiedler
 * Hiermit wird unentgeltlich jeder Person, die eine Kopie der Software und der zugehörigen Dokumentationen (die "Software") erhält, die Erlaubnis erteilt, sie uneingeschränkt zu nutzen, inklusive und ohne Ausnahme mit dem Recht, sie zu verwenden, zu verändern und Personen, denen diese Software überlassen wird, diese Rechte zu verschaffen, außer sie zu verteilen unter den folgenden Bedingungen:
 * <p>
 * Der obige Urheberrechtsvermerk und dieser Erlaubnisvermerk sind in allen Kopien oder Teilkopien der Software beizulegen.
 * <p>
 * DIE SOFTWARE WIRD OHNE JEDE AUSDRÜCKLICHE ODER IMPLIZIERTE GARANTIE BEREITGESTELLT, EINSCHLIEßLICH DER GARANTIE ZUR BENUTZUNG FÜR DEN VORGESEHENEN ODER EINEM BESTIMMTEN ZWECK SOWIE JEGLICHER RECHTSVERLETZUNG, JEDOCH NICHT DARAUF BESCHRÄNKT. IN KEINEM FALL SIND DIE AUTOREN ODER COPYRIGHTINHABER FÜR JEGLICHEN SCHADEN ODER SONSTIGE ANSPRÜCHE HAFTBAR ZU MACHEN, OB INFOLGE DER ERFÜLLUNG EINES VERTRAGES, EINES DELIKTES ODER ANDERS IM ZUSAMMENHANG MIT DER SOFTWARE ODER SONSTIGER VERWENDUNG DER SOFTWARE ENTSTANDEN.
 */
public class JoinHandler implements Listener {

    /**
     * Diese Klasse ist für den Spieler Join zuständig. Das {@link PlayerJoinEvent} wird nach dem {@link org.bukkit.event.player.AsyncPlayerPreLoginEvent} aufgerufen.
     * Hier finden die meisten Abfragen statt, die redis dann in den Memory-Cache lädt.
     * Debug Nachrichten sind hier wichtig, da hier viel schieflaufen kann, falls redis nicht verbunden ist.
     *
     * @param {@link PlayerJoinEvent} {@link Listener}
     */

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        if (RedisBuilder.getInstance().getConfig().getString("ServerType").equalsIgnoreCase("Lobby")) {
            event.getPlayer().getInventory().clear();
            ItemStack item = new ItemStack(Material.NETHER_STAR);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName("§7»§eNavigator");
            item.setItemMeta(meta);
            event.getPlayer().getInventory().setItem(0, item);
            event.setJoinMessage(null);
        }

        String name = event.getPlayer().getName();
        String uuid = UUIDFetcher.getUUID(name).toString();
        System.out.println(uuid.toString());
        System.out.println(name);
        DBUser dbUser = new DBUser(uuid.toString(), name);
        System.out.println(dbUser.toString());

        long millisNow = System.currentTimeMillis();

        if (!dbUser.userExists()) {
            dbUser.createUser();
            RedisBuilder.getInstance().getScoreManager().setPrefix(event.getPlayer());
            String millis = String.valueOf(System.currentTimeMillis() - millisNow);
            System.out.println("Backend -> Player Join took " + millis + " milliseconds");
            handleBroadcaster(Bukkit.getPlayer(name));
            Bukkit.getPlayer(name).chat("/spawn");
            Bukkit.getPlayer(name).sendMessage("§cDie ersten 40 Spieler bekommen bei 85 Stunden §clLifetime Premium §7kostenlos");
        } else {
            System.out.println("Backend -> User already exists!");
            String millis = String.valueOf(System.currentTimeMillis() - millisNow);
            System.out.println("Backend -> Player Join took " + millis + " milliseconds");
            handleBroadcaster(Bukkit.getPlayer(name));
            Bukkit.getPlayer(name).chat("/spawn");
            Bukkit.getPlayer(name).sendMessage("§cDie ersten 40 Spieler erhalten bei 85 Stunden Spielzeit §cLifetime Premium §7kostenlos");
        }
        RedisBuilder.getInstance().getScoreManager().setPrefix(event.getPlayer());


    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (RedisBuilder.getInstance().getConfig().getString("ServerType").equalsIgnoreCase("Lobby")) {
            event.setQuitMessage(null);
        }
    }

    public void handleBroadcaster(Player player) {
        player.setHealthScale(20);
        player.setFoodLevel(20);
        player.sendMessage("§8Willkommen auf §4MyTraz.net! Bitte beachte, das wir uns derweil in der Beta Phase befinden. Wir können daher nicht ausschließen, das Fehler auftreten werden.");
        player.sendMessage("§7--------------------------------------------------");
        player.sendMessage("§4Wichtig: §aDa wir uns mit der Hauptlobby noch in der Bauphase befinden , ist dies nur eine vorübergehende Lobby.");
        player.sendMessage("§7--------------------------------------------------");
        player.sendMessage("§7--------------------------------------------------");
        player.sendMessage("§cWir bieten im Moment nur Survival an, die MiniGames und Modded sind bald verüfgbar.");
        player.sendMessage("§7--------------------------------------------------");

        player.sendTitle("§7§lWillkommen auf", "§l§6MyTraz.net");
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 20F, 2F);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(RedisBuilder.getInstance(), new Runnable() {
            @Override
            public void run() {
                Random r = new Random();
                switch (r.nextInt(2)) {
                    case 1:
                        player.sendMessage("§cMyTraz ist ein Projekt von §l§6www.mediolutec.de!");
                        break;
                    case 2:
                        player.sendMessage("§cDie ersten 40 Spieler erhalten bei 85 Stunden Spielzeit §cLifetime Premium §7kostenlos");
                        break;
                    case 0:
                        player.sendMessage("§cDie ersten 40 Spieler erhalten bei 85 Stunden Spielzeit §cLifetime Premium §7kostenlos");
                        break;

                }

            }
        }, 2500L, 2500L);
    }


    public void setLobbyItems() {

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        try {
            if (RedisBuilder.getInstance().getConfig().getString("ServerType").equalsIgnoreCase("Lobby")) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                    if (event.getItem().getType() == Material.NETHER_STAR) {
                        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7»§eNavigator")) {
                            Inventory inv = Bukkit.createInventory(null, InventoryType.BREWING);
                            ItemStack survival = new ItemStack(Material.IRON_SWORD);
                            ItemMeta survivalMeta = survival.getItemMeta();
                            survivalMeta.setDisplayName("§7»§eSurvival");
                            survival.setItemMeta(survivalMeta);
                            inv.setItem(0, survival);
                            ItemStack barrier = new ItemStack(Material.BARRIER);
                            ItemMeta barrierMeta = barrier.getItemMeta();
                            barrierMeta.setDisplayName("§7»§cKommt noch!");
                            barrier.setItemMeta(barrierMeta);
                            inv.setItem(1, barrier);
                            inv.setItem(2, barrier);
                            inv.setItem(3, barrier);
                            event.getPlayer().openInventory(inv);
                        }
                    }
                }
            }
        } catch (Exception exception) {
        }


    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        try {
            if (RedisBuilder.getInstance().getConfig().getString("ServerType").equalsIgnoreCase("Lobby")) {
                System.out.println("lobby");
                System.out.println("netherstar");
                if (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase("§7»§eSurvival")) {
                    System.out.println("survival");
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);
                    try {
                        System.out.println("gets called");
                        out.writeUTF("Connect");
                        out.writeUTF("survival");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    Player player = (Player) event.getWhoClicked();
                    player.sendPluginMessage(RedisBuilder.getInstance(), "BungeeCord", b.toByteArray());

                }


            }
        } catch (Exception exception) {
        }


    }

    @EventHandler
    public void onLeaveDespawn(LeavesDecayEvent event) {
        if (RedisBuilder.getInstance().getConfig().getString("ServerType").equalsIgnoreCase("Lobby")) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onClick2(InventoryClickEvent event) {
        if (RedisBuilder.getInstance().getConfig().getString("ServerType").equalsIgnoreCase("Lobby")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (RedisBuilder.getInstance().getConfig().getString("ServerType").equalsIgnoreCase("Lobby")) {
            event.setCancelled(true);
        }
    }

}

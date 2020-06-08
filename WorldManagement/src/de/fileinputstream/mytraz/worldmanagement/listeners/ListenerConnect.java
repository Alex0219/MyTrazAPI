package de.fileinputstream.mytraz.worldmanagement.listeners;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.chatlog.entry.ChatEntry;
import de.fileinputstream.mytraz.worldmanagement.rank.DBUser;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import de.fileinputstream.mytraz.worldmanagement.uuid.NameTags;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import net.minecraft.server.v1_15_R1.IChatBaseComponent;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

/**
 * User: Alexander<br/>
 * Date: 22.02.2018<br/>
 * Time: 20:49<br/>
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
public class ListenerConnect implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

        Bukkit.broadcastMessage("§bAlex0219.de §7» §c" + event.getPlayer().getDisplayName() + " §7ist §cgestorben!");
        String uuid = UUIDFetcher.getUUID(event.getPlayer().getName()).toString();
        if (Bootstrap.getInstance().getWorldManager().hasWorld(uuid)) {
            new WorldCreator(Bootstrap.getInstance().getWorldManager().getWorld(uuid)).createWorld();
            event.setRespawnLocation(Bukkit.getWorld(Bootstrap.getInstance().getWorldManager().getWorld(uuid)).getSpawnLocation());
        } else {
            event.setRespawnLocation(Bukkit.getWorld(Bootstrap.getInstance().getConfig().getString("SpawnWorld")).getSpawnLocation());
        }

    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = event.getEntity();
            event.setDeathMessage(null);
        }

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        DBUser dbUser = new DBUser(event.getPlayer().getUniqueId().toString(), event.getPlayer().getName());
        System.out.println(dbUser.toString());

        long millisNow = System.currentTimeMillis();

        if (!dbUser.userExists()) {
            dbUser.createUser();
            String millis = String.valueOf(System.currentTimeMillis() - millisNow);
            System.out.println("Backend -> Player Join took " + millis + " milliseconds");

        } else {
            System.out.println("Backend -> User already exists!");
            String millis = String.valueOf(System.currentTimeMillis() - millisNow);
            System.out.println("Backend -> Player Join took " + millis + " milliseconds");
        }
        Player player = event.getPlayer();
        String rank = RankManager.getRank(event.getPlayer().getUniqueId().toString());
        Bootstrap.getInstance().getChatLogManager().chatLogs.put(dbUser.getUuid(), new ArrayList<ChatEntry>());
        NameTags.setTags(player);


        event.setJoinMessage(event.getPlayer().getDisplayName() + " §7ist dem Server beigetreten.");
        //Remove cooldown that was added in minecraft 1.9
        player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);
        //sendTablist(player, "§l§4Alex0219.de §7Dein Minecraft Netzwerk!", "§7Teamspeak: Alex0219.de");
        LocalDate localDate = LocalDate.now();
        Locale locale = new Locale("de", "DE");
        String date = localDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM, yyyy", locale));

        //Bukkit.getOnlinePlayers().forEach(p -> {
        //  Bukkit.getScheduler().runTaskTimer(Bootstrap.getInstance(), () -> {
        //    new ListenerConnect().sendTablist(p, "§l§4Alex0219.de §7Dein Minecraft Netzwerk!\n", "§7Teamspeak: Alex0219.de.DE \n" + date + "\n§cSpieler online: §e" + Bukkit.getOnlinePlayers().size());
        //}, 1, 1);
        //});
        if (player.getWorld().getName().equalsIgnoreCase("world")) {
            player.setHealthScale(20.0);
            player.setFoodLevel(20);
        }

        player.sendMessage("           §7*---*---*         ");
        player.sendMessage(" ");
        player.sendMessage("     §7Willkommen auf §a§lAlex0219.de");
        player.sendMessage(" ");
        player.sendMessage("           §7*---*---*           ");


    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("§c" + event.getPlayer().getDisplayName() + " §7hat den Server verlassen.");
        Player player = event.getPlayer();
        String uuid = event.getPlayer().getUniqueId().toString();
        if (Bootstrap.getInstance().getWorldManager().hasWorld(uuid)) {
            String world = Bootstrap.getInstance().getWorldManager().getWorld(uuid);
            Bukkit.unloadWorld(world, true);
            System.out.println("Backend -> Unloading world from player: " + player.getName() + "(uuid:" + uuid + ").");
        }
    }

    public void sendTablist(Player p, String header, String footer) {
        IChatBaseComponent tabheader = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + header + "\"}");
        IChatBaseComponent tabfooter = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + footer + "\"}");
        PacketPlayOutPlayerListHeaderFooter tablist = new PacketPlayOutPlayerListHeaderFooter();
        try {
            Field headerField = tablist.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(tablist, tabheader);
            headerField.setAccessible(!headerField.isAccessible());
            Field footerField = tablist.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(tablist, tabfooter);
            footerField.setAccessible(!footerField.isAccessible());
        } catch (Exception var11) {
            var11.printStackTrace();
        } finally {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(tablist);
        }
    }

    @EventHandler
    public void onPerfomCommand(PlayerCommandPreprocessEvent e) {
        String msg = e.getMessage();
        if ((msg.equalsIgnoreCase("/plugins")) || (msg.equalsIgnoreCase("/pl")) || (msg.startsWith("/bukkit:plugins")) || (msg.startsWith("/bukkit:pl")) || ((msg.startsWith("/bukkit:ver") | msg.startsWith("/bukkit:version"))) || (msg.startsWith("/ver")) || (msg.startsWith("/version")) || (msg.startsWith("/?")) || (msg.equalsIgnoreCase("/help")) || (msg.startsWith("/bukkit:?")) || (msg.equalsIgnoreCase("/bukkit:?")) || (msg.equalsIgnoreCase("/bukkit:help")) || (msg.equalsIgnoreCase("/icanhasbukkit")) || (msg.equalsIgnoreCase("/me")) || (msg.startsWith("/minecraft:me"))) {
            if (!e.getPlayer().hasPermission("api.bypass")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("Unknown command. Type \"help\" for help.");
            } else {
            }

        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity().getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity().getWorld().getName().equalsIgnoreCase("world")) {
            //keep the current food level
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityExplode(final EntityExplodeEvent event) {
        if (event.getEntity() instanceof Creeper) {
            event.setCancelled(true);
        }
    }


}
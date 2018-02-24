package de.fileinputstream.mytraz.api.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.ArrayList;

/**
 * User: Alexander<br/>
 * Date: 04.01.2018<br/>
 * Time: 21:40<br/>
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
public class ExternalCommands implements CommandExecutor, Listener {

    public static String prefix = "§cSystem §7● ";
    public static ArrayList<Player> vanish = new ArrayList();
    public static ArrayList<Player> fly = new ArrayList();

    public static void setGameMode(Player p, String[] args, GameMode gm) {
        if (args.length == 2) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target != null) {
                target.setGameMode(gm);
                target.sendMessage(prefix + "§7Dein Gamemode wurde auf §b" + gm.toString() + " §7gesetzt.");
                p.sendMessage(prefix + "§7Du hast den Gamemode von §b" + target.getName() + " §7auf §b" + gm.toString() + " §7gesetzt.");
            } else {
                p.sendMessage(prefix + "§cDieser Spieler ist nicht online!");
            }
        } else {
            p.setGameMode(gm);
            p.sendMessage(prefix + "§7Du hast deinen Gamemode auf §b" + gm.toString() + " §7gesetzt.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("gamemode")) {
            if (!p.hasPermission("lobby.gamemode")) {
                return true;
            }
            if (args.length == 0) {
                p.sendMessage(prefix + "§7/gamemode [0/1/2/3] [Spieler]");
            } else if (args[0].equalsIgnoreCase("0")) {
                setGameMode(p, args, GameMode.SURVIVAL);
            } else if (args[0].equalsIgnoreCase("1")) {
                setGameMode(p, args, GameMode.CREATIVE);
            } else if (args[0].equalsIgnoreCase("2")) {
                setGameMode(p, args, GameMode.ADVENTURE);
            } else if (args[0].equalsIgnoreCase("3")) {
                setGameMode(p, args, GameMode.SPECTATOR);
            }
        }
        Player t1;
        if (cmd.getName().equalsIgnoreCase("tp")) {
            if (!p.hasPermission("lobby.teleport")) {
                return true;
            }
            if (args.length == 0) {
                p.sendMessage(prefix + "§7/tp [Spieler] [Spieler]");
            } else if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    p.teleport(target);
                    p.sendMessage(prefix + "§7Du wurdest zu §b" + target.getName() + " §7teleportiert.");
                } else {
                    p.sendMessage(prefix + "§cDieser Spieler ist nicht online!");
                }
            } else if (args.length == 2) {
                t1 = Bukkit.getPlayer(args[0]);
                if (t1 == null) {
                    p.sendMessage(prefix + "§cDer Spieler §b" + args[0] + " §cist nicht online!");
                    return true;
                }
                Player t2 = Bukkit.getPlayer(args[1]);
                if (t2 == null) {
                    p.sendMessage(prefix + "§cDer Spieler §b" + args[1] + " §cist nicht online!");
                    return true;
                }
                t1.teleport(t2);
                p.sendMessage(prefix + "§7Du hast den Spieler §b" + t1.getName() + " §7zu §b" + t2.getName() + " ?7teleportiert.");
            }
        }
        if (cmd.getName().equalsIgnoreCase("vanish")) {
            if (!p.hasPermission("lobby.vanish")) {
                return true;
            }
            if (!vanish.contains(p)) {
                vanish.add(p);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.hidePlayer(p);
                }

                p.sendMessage(prefix + "§7Du bist nun im Vanish.");
            } else {
                vanish.remove(p);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.showPlayer(p);
                }
                p.sendMessage(prefix + "§7Du bist nun nicht mehr im Vanish.");
            }
        }
        if (cmd.getName().equalsIgnoreCase("fly")) {
            if (!p.hasPermission("lobby.fly")) {
                return true;
            }
            if (!fly.contains(p)) {
                p.setAllowFlight(true);
                p.setFlying(true);
                fly.add(p);
                p.sendMessage(prefix + "§7Du fliegst nun.");
            } else {
                p.setAllowFlight(false);
                fly.remove(p);
                p.setFlying(false);
                p.sendMessage(prefix + "§7Du fliegst nun nicht mehr.");
            }
        }
        return true;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            if (vanish.contains(all)) {
                event.getPlayer().hidePlayer(all);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        if (vanish.contains(player)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (vanish.contains(player)) {
            event.setCancelled(true);
        }
    }
}

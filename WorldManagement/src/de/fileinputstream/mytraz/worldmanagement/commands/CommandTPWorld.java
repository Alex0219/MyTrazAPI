package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;

import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * User: Alexander<br/>
 * Date: 07.02.2018<br/>
 * Time: 19:02<br/>
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
public class CommandTPWorld implements CommandExecutor {

    public CommandTPWorld() {
        System.out.println("Gets called");
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            String uuid = UUIDFetcher.getUUID(player.getName()).toString();
            String rank = RankManager.getRank(UUIDFetcher.getUUID(player.getName()).toString());
            if (args.length == 0) {
                if (Bootstrap.getInstance().getWorldManager().hasWorld(uuid)) {
                    String world = getWorld(uuid);
                    System.out.println(world);
                    if (Bootstrap.getInstance().getWorldManager().getWorldResidentsFile(world).exists()) {
                        new WorldCreator(world).createWorld();
                        player.teleport(Bukkit.getServer().getWorld(world).getSpawnLocation());
                    } else {
                        player.sendMessage("§bFlippiGames §7» §cDiese Welt existiert nicht!");
                        return true;
                    }

                } else {
                    player.sendMessage("§bFlippiGames §7» Du besitzt keine Welt.");
                    return true;
                }
            } else if (args.length == 1) {
                if (getWorld(uuid).equalsIgnoreCase(args[0])) {
                    if (rank.equalsIgnoreCase("admin") || rank.equalsIgnoreCase("sup") || rank.equalsIgnoreCase("mod")) {


                    }


                } else {
                    if (rank.equalsIgnoreCase("admin") || rank.equalsIgnoreCase("sup") || rank.equalsIgnoreCase("mod")) {
                        File file = new File(Bukkit.getServer().getWorldContainer(), args[0]);

                        if (file.exists()) {
                            new WorldCreator(args[0]).createWorld();
                            player.teleport(Bukkit.getWorld(args[0]).getSpawnLocation());
                            return true;
                        } else {
                            player.sendMessage("§bFlippiGames §7» §cDiese Welt existiert nicht!");
                            return true;
                        }

                    } else if (Bootstrap.getInstance().getWorldManager().isResidentInWorld(uuid, args[0])) {
                        if (Bootstrap.getInstance().getWorldManager().getWorldResidentsFile(args[0]).exists()) {
                            new WorldCreator(args[0]).createWorld();
                            player.teleport(Bukkit.getServer().getWorld(args[0]).getSpawnLocation());
                        } else {
                            player.sendMessage("§bFlippiGames §7» §cDiese Welt existiert nicht!");
                            return true;
                        }

                    } else {
                        player.sendMessage("§bFlippiGames §7» Du darfst dich nicht in diese Welt teleportieren.");
                        return true;
                    }

                }


            } else {
                player.sendMessage("§bFlippiGames §7» Bitte verwende /tpworld oder /tpworld <ID>");
            }

        } else {
            System.out.println("Not a player!");
            return true;
        }
        return false;
    }

    public String getWorld(String uuid) {

        if (Bootstrap.getInstance().getWorldManager().hasWorld(uuid)) {
            String worldString = Bootstrap.getInstance().getJedis().hget("uuid:" + uuid, "worlds");
            System.out.println(worldString);
            ArrayList<String> playerWorlds = new ArrayList<String>(Arrays.asList(worldString));
            String world = playerWorlds.get(0).replace("[", "").replace("]", "");

            return world;

        }
        return "";
    }
}

package de.fileinputstream.mytraz.worldmanagement.commands;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import de.fileinputstream.mytraz.worldmanagement.rank.RankManager;
import de.fileinputstream.mytraz.worldmanagement.uuid.UUIDFetcher;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * User: Alexander<br/>
 * Date: 04.02.2018<br/>
 * Time: 21:46<br/>
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
public class CommandWorldInfo implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String uuid = UUIDFetcher.getUUID(player.getName()).toString();
            String rank = RankManager.getRank(uuid);
            if (rank.equalsIgnoreCase("admin") || rank.equalsIgnoreCase("sup") || rank.equalsIgnoreCase("mod") || rank.equalsIgnoreCase("partner")) {
                if (args.length == 1) {
                    String playername = args[0];
                    String targetUUID = UUIDFetcher.getUUID(playername).toString();
                    player.sendMessage("§7===========§a" + playername + "§7==============");
                    player.sendMessage("§aInfo über:§4 " + playername);
                    player.sendMessage("§aWelten:§6 " + Bootstrap.getInstance().getWorldManager().getWorld(targetUUID));
                    player.sendMessage("§7===========§a" + playername + "§7==============");
                    if (RankManager.getRank(targetUUID).equalsIgnoreCase("")) {
                        player.sendMessage("§bAlex0219.de §7» Dieser Spieler existiert nicht!");

                    }
                } else {
                    player.sendMessage("§bAlex0219.de §7» Verwende /worldinfo <Spieler>");
                    return true;
                }
            } else {

            }
        } else {
            sender.sendMessage("§bAlex0219.de §7» Nur Spieler können diesen Befehö ausführen.");
        }
        return false;
    }
}

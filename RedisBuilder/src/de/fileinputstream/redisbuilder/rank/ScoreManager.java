package de.fileinputstream.redisbuilder.rank;

import de.fileinputstream.redisbuilder.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

/**
 * User: Alexander<br/>
 * Date: 25.02.2018<br/>
 * Time: 15:07<br/>
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
public class ScoreManager {

    Scoreboard sb;

    public void initPrefix() {
        /*




        } else if (s.equalsIgnoreCase("sup".toLowerCase())) {
            setPlayer(player, "sup");
            player.setPlayerListName("§1" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("architekt".toLowerCase())) {
            setPlayer(player, "builder");
            player.setPlayerListName("§2" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("youtuber".toLowerCase())) {
            setPlayer(player, "youtube");
            player.setPlayerListName("§5" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("premium".toLowerCase())) {
            setPlayer(player, "premium");
            player.setPlayerListName("§6" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("spieler".toLowerCase())) {
            setPlayer(player, "spieler");
            player.setPlayerListName("§7" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("mod".toLowerCase())) {
            setPlayer(player, "mod");
            player.setPlayerListName("§cM" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("bauleitung".toLowerCase())) {
            setPlayer(player, "bauleitung");
            player.setPlayerListName("§2§l" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("teamleitung".toLowerCase())) {
            setPlayer(player, "teamleitung");
            player.setPlayerListName("§c§l" + player.getName());
            return;
            //§8[§8] §8[§cM§8]§c" + p.getName() + "§7 » "

        }
         */
        sb = Bukkit.getScoreboardManager().getNewScoreboard();
        sb.registerNewTeam("0001A");
        sb.registerNewTeam("0002D");
        sb.registerNewTeam("0003M");
        sb.registerNewTeam("0004S");
        sb.registerNewTeam("0005BL");
        sb.registerNewTeam("0006B");
        sb.registerNewTeam("0007YT");
        sb.registerNewTeam("0008Sp");

        sb.getTeam("0001A").setPrefix("§4§l");
        sb.getTeam("0002D").setPrefix("§3");
        sb.getTeam("0003M").setPrefix("§c");
        sb.getTeam("0004S").setPrefix("§1");
        sb.getTeam("0005BL").setPrefix("§2§l");
        sb.getTeam("0006B").setPrefix("§2");
        sb.getTeam("0007YT").setPrefix("§5");
        sb.getTeam("0008Sp").setPrefix("§7");

    }

    public void setPrefix(Player player) {
        String team = "";
        String uuid = UUIDFetcher.getUUID(player.getName()).toString();
        String s = RankManager.getRank(uuid);

        if (s.equalsIgnoreCase("admin".toLowerCase())) {
            team = "0001A";
        } else if (s.equalsIgnoreCase("dev".toLowerCase())) {
            team = "0002D";
        } else if (s.equalsIgnoreCase("sup".toLowerCase())) {
            team = "0004S";
        } else if (s.equalsIgnoreCase("architekt".toLowerCase())) {
            team = "0006B";
        } else if (s.equalsIgnoreCase("youtuber".toLowerCase())) {
            team = "0007YT";
        } else if (s.equalsIgnoreCase("spieler".toLowerCase())) {
            team = "0008Sp";
        } else if (s.equalsIgnoreCase("mod".toLowerCase())) {
            team = "0003M";
        } else if (s.equalsIgnoreCase("bauleitung".toLowerCase())) {
            team = "0005BL";

        }
        sb.getTeam(team).addPlayer(player);
        player.setDisplayName(sb.getTeam(team).getPrefix() + player.getName());
        player.setPlayerListName(sb.getTeam(team).getPrefix() + player.getName());
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.setScoreboard(sb);
        }
    }
}

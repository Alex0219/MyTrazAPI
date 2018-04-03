package de.fileinputstream.mytraz.bungee.manager;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Alexander<br/>
 * Date: 04.03.2018<br/>
 * Time: 14:47<br/>
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
public class MuteManager {

    static Configuration cfg = Files.MuteConfig;

    public static boolean exists(String playername) {
        return cfg.get("Players." + UUIDFetcher.getUUID(playername)) != null;
    }

    public static void createPlayer(String playername) {
        if (!exists(playername)) {
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".Playername",
                    playername);
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".Muted",
                    Boolean.valueOf(false));
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".Reason", "");
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".By", "");
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".End", Long.valueOf(0L));
            Files.saveMuteFile();
        }
    }

    public static boolean isMuted(String playername) {
        if (exists(playername)) {
            return cfg.getBoolean("Players." + UUIDFetcher.getUUID(playername) +
                    ".Muted");
        }
        return false;
    }

    public static void Mute(String playername, String Reason, String MutedBy, int seconds) {
        if (!isMuted(playername)) {
            long currentm = System.currentTimeMillis();
            long current = currentm / 1000L;
            long end = current + seconds;
            if (seconds == -1) {
                end = -1L;
            }
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".Playername",
                    playername);
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".Muted",
                    Boolean.valueOf(true));
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".Reason",
                    Reason);
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".By",
                    MutedBy);
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".End", Long.valueOf(end));
            Files.saveBanFile();

            List muted11;
            if (cfg.getStringList("MutedPlayers") != null) {
                muted11 = cfg.getStringList("MutedPlayers");
            } else {
                muted11 = new ArrayList();
            }
            muted11.add(playername);
            cfg.set("MutedPlayers", muted11);
            Files.saveMuteFile();
            for (ProxiedPlayer o : BungeeCord.getInstance().getPlayers()) {
                if (o.hasPermission("server.team")) {
                    o.sendMessage("§7§m-------------§bCloud §7»§7§m-------------");
                    o.sendMessage("§cTyp §7» §4MUTE");
                    o.sendMessage("§7Spieler »  §c " + playername);
                    o.sendMessage("§7Von » §c" + MutedBy);
                    o.sendMessage("§7Dauer §7» §c" + getRemainingTime(playername));
                    o.sendMessage("§7§m-------------§bCloud §7»§7§m-------------");
                }
            }
        }
    }

    public static void unMute(String playername, String By) {
        if (isMuted(playername)) {
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".Playername",
                    playername);
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".Muted",
                    Boolean.valueOf(false));
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".Reason", "");
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".By", "");
            cfg.set("Players." + UUIDFetcher.getUUID(playername) + ".End", Long.valueOf(0L));
            Files.saveBanFile();

            List banned = cfg.getStringList("MutedPlayers");
            banned.remove(playername);
            cfg.set("MutedPlayers", banned);
            Files.saveMuteFile();
            for (ProxiedPlayer o : BungeeCord.getInstance().getPlayers()) {
                if (o.hasPermission("server.team")) {
                    o.sendMessage("§7§m-------------§bCloud §7»§7§m-------------");
                    o.sendMessage("§cTyp » §4UNMUTE");
                    o.sendMessage("§7Spieler §7»  §c " + playername);
                    o.sendMessage("§7Von » §c" + By);
                    o.sendMessage("§7§m-------------§bCloud §7»§7§m-------------");
                }
            }
        }
    }

    public static List<String> getMutedPlayers() {
        return cfg.getStringList("MutedPlayers");
    }

    public static String getReason(String playername) {
        String reason = "";
        if (isMuted(playername)) {
            reason = cfg.getString("Players." + UUIDFetcher.getUUID(playername) +
                    ".Reason");
        }
        return reason;
    }

    public static String getWhoMuted(String playername) {
        String whomuted = "";
        if (isMuted(playername)) {
            whomuted = cfg.getString("Players." +
                    UUIDFetcher.getUUID(playername) + ".By");
        }
        return whomuted;
    }

    public static long getEnd(String playername) {
        long end = -1L;
        if (isMuted(playername)) {
            end = cfg.getLong("Players." + UUIDFetcher.getUUID(playername) +
                    ".End");
        }
        return end;
    }

    public static String getRemainingTime(String Spielername) {
        String remainingTime = "";
        if (isMuted(Spielername)) {
            long currentm = System.currentTimeMillis();
            long current = currentm / 1000L;
            long end = getEnd(Spielername);
            long difference = end - current;
            if (end == -1L) {
                return "§cPERMANENT";
            }
            int Minuten = 0;
            int Stunden = 0;
            int Tage = 0;
            while (difference >= 60L) {
                difference -= 60L;
                Minuten++;
            }
            while (Minuten >= 60) {
                Minuten -= 60;
                Stunden++;
            }
            while (Stunden >= 24) {
                Stunden -= 24;
                Tage++;
            }
            remainingTime =
                    "§e" + Tage + " Tag(e), " + Stunden + " Stunde(n), " + Minuten + " Minute(n)  ";
        }
        return remainingTime;
    }
}

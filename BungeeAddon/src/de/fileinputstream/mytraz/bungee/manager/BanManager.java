package de.fileinputstream.mytraz.bungee.manager;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Alexander<br/>
 * Date: 04.03.2018<br/>
 * Time: 17:09<br/>
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
public class BanManager {
    static Configuration cfg = Files.BanConfig;

    public static boolean exists(String playername) {
        return cfg.get("Spieler." + UUIDFetcher.getUUID(playername)) != null;
    }

    public static void createPlayer(String Spielername) {
        if (!exists(Spielername)) {
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".Spielername",
                    Spielername);
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".Ban",
                    Boolean.valueOf(false));
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".Grund", "");
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".von", "");
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".Ende", Long.valueOf(0L));
            Files.saveBanFile();
        }
    }

    public static boolean isBanned(String Spielername) {
        if (exists(Spielername)) {
            return cfg.getBoolean("Spieler." + UUIDFetcher.getUUID(Spielername) +
                    ".Ban");
        }
        return false;
    }

    public static void Ban(String Spielername, String Grund, String von, int Sekunden) {
        if (!isBanned(Spielername)) {
            long currentm = System.currentTimeMillis();
            long current = currentm / 1000L;
            long end = current + Sekunden;
            if (Sekunden == -1) {
                end = -1L;
            }
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".Spielername",
                    Spielername);
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".Ban",
                    Boolean.valueOf(true));
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".Grund",
                    Grund);
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".von",
                    von);
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".Ende", Long.valueOf(end));
            Files.saveBanFile();
            ProxiedPlayer target = BungeeCord.getInstance().getPlayer(
                    Spielername);
            if (target != null) {
                target.disconnect(getBannedMessage(Spielername));
            }
            List banned11;

            if (cfg.getStringList("GebannteSpieler") != null) {
                banned11 = cfg.getStringList("GebannteSpieler");
            } else {
                banned11 = new ArrayList();
            }
            banned11.add(Spielername);
            cfg.set("GebannteSpieler", banned11);
            Files.saveBanFile();
            for (ProxiedPlayer o : BungeeCord.getInstance().getPlayers()) {
                if (o.hasPermission("server.team")) {
                    o.sendMessage("§7§m-------------§bFlippiGames §7»§7§m-------------");
                    o.sendMessage("§7Typ » §4BAN");
                    o.sendMessage("§7Spieler »  §c " + Spielername);
                    o.sendMessage("§7Banndauer »  §c " + getRemainingTime(Spielername));
                    o.sendMessage("§7Grund »  §c " + getReason(Spielername));
                    o.sendMessage("§7Von » §c" + von);
                    o.sendMessage("§7§m-------------§bFlippiGames §7»§7§m-------------");
                }
            }
        }
    }

    public static void unBan(String Spielername, String von) {
        if (isBanned(Spielername)) {
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".Spielername",
                    Spielername);
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".Ban",
                    Boolean.valueOf(false));
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".Grund", "");
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".von", "");
            cfg.set("Spieler." + UUIDFetcher.getUUID(Spielername) + ".Ende", Long.valueOf(0L));
            Files.saveBanFile();

            List Ban = cfg.getStringList("GebannteSpieler");
            Ban.remove(Spielername);
            cfg.set("GebannteSpieler", Ban);
            Files.saveBanFile();
            for (ProxiedPlayer o : BungeeCord.getInstance().getPlayers()) {
                if (o.hasPermission("server.team")) {
                    o.sendMessage("§7§m-------------§bFlippiGames §7»§7§m-------------");
                    o.sendMessage("§7Typ » §4UNBAN");
                    o.sendMessage("§7Spieler »  §c " + Spielername);
                    o.sendMessage("§7Von » §c " + von);
                    o.sendMessage("§7§m-------------§bFlippiGames §7»§7§m-------------");
                }
            }
        }
    }

    public static List<String> getBannedPlayers() {
        return cfg.getStringList("GebannteSpieler");
    }

    public static String getReason(String Spielername) {
        String Grund = "";
        if (isBanned(Spielername)) {
            Grund = cfg.getString("Spieler." + UUIDFetcher.getUUID(Spielername) +
                    ".Grund");
        }
        return Grund;
    }

    public static String getWhoBanned(String Spielername) {
        String whobanned = "";
        if (isBanned(Spielername)) {
            whobanned = cfg.getString("Spieler." +
                    UUIDFetcher.getUUID(Spielername) + ".von");
        }
        return whobanned;
    }

    public static long getEnd(String Spielername) {
        long end = -1L;
        if (isBanned(Spielername)) {
            end = cfg.getLong("Spieler." + UUIDFetcher.getUUID(Spielername) +
                    ".Ende");
        }
        return end;
    }

    public static String getRemainingTime(String Spielername) {
        String remainingTime = "";
        if (isBanned(Spielername)) {
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

    public static String getBannedMessage(String Spielername) {
        String BanMsg = "";
        if (isBanned(Spielername)) {
            if (getEnd(Spielername) != -1L) {
                BanMsg =
                        "§7Du wurdest vom §cMyTraz §7Netzwerk gebannt! \n§7Grund » §c" + getReason(Spielername) + "\n§7Dauer » §c" + getRemainingTime(Spielername);
            } else {
                BanMsg =
                        "§7Du wurdest vom §cMyTraz §7Netzwerk gebannt! \n§7Grund » §c" + getReason(Spielername) + "\n§7Dauer » §cpermanent";
            }
        }
        return BanMsg;
    }

    public boolean canBan(String uuidBanner, String uuidBanned) {
        return false;
    }
}
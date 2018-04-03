package de.fileinputstream.mytraz.worldmanagement.uuid;


import de.fileinputstream.redisbuilder.rank.RankManager;
import de.fileinputstream.redisbuilder.uuid.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;


public class NameTags {

    public static HashMap<UUID, String> userCache = new HashMap<>();
    private static HashMap<String, Object> teams = new HashMap<>();

    public static void updateTeams() {
        for (Map.Entry entry : teams.entrySet()) {
            Object packet = teams.get(entry.getKey());

            for (Player all : Bukkit.getOnlinePlayers()) {
                try {
                    Constructor<?> scoreboardTeamConstructor = getNMSClass("PacketPlayOutScoreboardTeam")
                            .getConstructor(getNMSClass("ScoreboardTeam"), int.class);
                    sendPacket(all, scoreboardTeamConstructor.newInstance(entry.getValue(), 1));
                    sendPacket(all, scoreboardTeamConstructor.newInstance(entry.getValue(), 0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }


    public static void addToTeam(Player player) {
        teams.entrySet().forEach(entry -> {
            Object packet = teams.get(entry.getKey());
            try {
                Field f = packet.getClass().getDeclaredField("c");
                f.setAccessible(true);
                Set<String> list = new HashSet<>();
                list.addAll((Collection<? extends String>) f.get(packet));
                if (list.contains(player.getName())) {
                    list.remove(player.getName());
                    setField(packet, "c", list);
                    teams.put(entry.getKey(), packet);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        String uuid = UUIDFetcher.getUUID(player.getName()).toString();
        String s = RankManager.getRank(uuid);
        if (s.equalsIgnoreCase("admin".toLowerCase())) {
            setPlayer(player, "admin");
            player.setPlayerListName("§4§l" + player.getName());
            return;
        } else if (s.equalsIgnoreCase("dev".toLowerCase())) {
            setPlayer(player, "dev");
            player.setPlayerListName("§3" + player.getName());
            return;
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
            player.setPlayerListName("§c" + player.getName());
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


    }

    public static void initScoreboardTeams() {
        String suffix = "";
        try {

            Constructor<?> boardConstructor = getNMSClass("Scoreboard").getConstructor();
            Object board = boardConstructor.newInstance();
            init("00001O", "owner", "§4Owner" + suffix, board);
            init("00002A", "admin", "§4§l" + suffix, board);
            init("00003TL", "teamleitung", "§c§l" + suffix, board);
            init("00004D", "dev", "§3" + suffix, board);
            init("00005M", "mod", "§c" + suffix, board);
            init("00006S", "sup", "§1" + suffix, board);
            init("00007BL", "bauleitung", "§2§l" + suffix, board);
            init("00008Builder", "builder", "§2" + suffix, board);
            init("00009YT", "youtube", "§5" + suffix, board);
            init("00010Premium", "premium", "§6", board);
            init("0011Spieler", "spieler", "§7", board);

        } catch (Exception e) {
            e.printStackTrace();
            Bukkit.getConsoleSender().sendMessage("§4Die Scoreboard Teams konnten nicht initialisiert werden!");
        }

    }

    private static void init(String teamname, String HashMapName, String prefix, Object board) {

        try {

            Constructor<?> teamConstructor = getNMSClass("ScoreboardTeam").getConstructor(getNMSClass("Scoreboard"),
                    String.class);

            Object packet = teamConstructor.newInstance(board, teamname);

            Object enumVisibility = getNMSClass("ScoreboardTeamBase").getDeclaredClasses()[0].getField("ALWAYS")
                    .get(null);
            setField(packet, "j", enumVisibility);

            setField(packet, "e", prefix);

            teams.put(HashMapName, packet);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void setPlayer(Player p, String HashMapName) {
        Object packet = teams.get(HashMapName);
        try {
            Field f = packet.getClass().getDeclaredField("c");
            f.setAccessible(true);
            Set<String> list = new HashSet<>();
            list.addAll((Collection<? extends String>) f.get(packet));
            list.add(p.getName());
            setField(packet, "c", list);

            teams.put(HashMapName, packet);

            for (Map.Entry e : teams.entrySet()) {

                Constructor<?> scoreboardTeamConstructor = getNMSClass("PacketPlayOutScoreboardTeam")
                        .getConstructor(getNMSClass("ScoreboardTeam"), int.class);
                Object teampacket = scoreboardTeamConstructor.newInstance(e.getValue(), 0);
                sendPacket(p, teampacket);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setField(Object object, String fieldname, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldname);
            field.setAccessible(true);
            field.set(object, value);
            field.setAccessible(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class<?> getNMSClass(String name) {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}

package de.fileinputstream.mytraz.worldmanagement.scoreboard;

import de.fileinputstream.mytraz.worldmanagement.Bootstrap;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alexander on 07.06.2020 19:26
 * © 2020 Alexander Fiedler
 */
public class CustomScoreboard {

    @Getter
    private final String name, criterion;

    private final Scoreboard bukkitScoreboard;
    private final Objective obj;
    @Getter
    String title;
    @Getter
    private Row[] rows = new Row[0];
    private List<Row> rowCache = new ArrayList<>();
    private boolean finished = false;

    public CustomScoreboard(String name, String criterion, String title) {
        this.name = name;
        this.criterion = criterion;
        this.title = title;

        this.bukkitScoreboard = Bootstrap.getInstance().getMainScoreboard();
        this.obj = this.bukkitScoreboard.registerNewObjective(name, criterion);

        this.obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.obj.setDisplayName(title);
    }

    public void setTitle(String title) {
        this.title = title;

        this.obj.setDisplayName(title);
    }

    public void display(Player player) {
        player.setScoreboard(this.bukkitScoreboard);
    }

    public @Nullable
    Row addRow(String message) {
        if (this.finished) {
            new NullPointerException("Can not add rows if scoreboard is already finished").printStackTrace();
            return null;
        }

        try {
            final Row row = new Row(this, message, rows.length);

            this.rowCache.add(row);

            return row;
        } catch (Exception e) {
            return null;
        }
    }

    public void finish() {
        if (this.finished) {
            new NullPointerException("Can not finish if scoreboard is already finished").printStackTrace();
            return;
        }

        this.finished = true;

        for (int i = rowCache.size() - 1; i >= 0; i--) {
            final Row row = rowCache.get(i);

            final Team team = this.bukkitScoreboard.registerNewTeam(name + "." + criterion + "." + (i + 1));
            team.addEntry(ChatColor.values()[i] + "");
            this.obj.getScore(ChatColor.values()[i] + "").setScore(rowCache.size() - i);

            row.team = team;
            row.setMessage(row.message);
        }

        this.rows = rowCache.toArray(new Row[rowCache.size()]);
    }


    public static class Row {

        @Getter
        private final CustomScoreboard scoreboard;
        @Getter
        private final int rowInScoreboard;
        private Team team;
        @Getter
        private String message;

        public Row(CustomScoreboard sb, String message, int row) {
            this.scoreboard = sb;
            this.rowInScoreboard = row;
            this.message = message;
        }

        private static String[] splitStringWithChatcolorInHalf(String str) {
            final String[] strs = new String[2];

            ChatColor cc1 = ChatColor.WHITE, cc2 = null;
            Character lastChar = null;

            strs[0] = "";
            for (int i = 0; i < str.length() / 2; i++) {
                final char c = str.charAt(i);

                if (lastChar != null) {
                    final ChatColor cc = charsToChatColor(new char[]{lastChar, c});

                    if (cc != null) {
                        if (cc.isFormat())
                            cc2 = cc;
                        else {
                            cc1 = cc;
                            cc2 = null;
                        }
                    }
                }

                strs[0] += c;
                lastChar = c;
            }

            strs[1] = (cc1 != null ? cc1 : "") + "" + (cc2 != null ? cc2 : "") + str.substring(str.length() / 2);

            return strs;
        }

        private static @Nullable
        ChatColor charsToChatColor(char[] chars) {
            for (ChatColor cc : ChatColor.values()) {
                final char[] ccChars = cc.toString().toCharArray();

                int same = 0;
                for (int i = 0; i < 2; i++) {
                    if (ccChars[i] == chars[i])
                        same++;
                }

                if (same == 2) return cc;
            }

            return null;
        }

        public void setMessage(String message) {
            this.message = message;

            if (scoreboard.finished) {
                final String[] parts = splitStringWithChatcolorInHalf(message);

                this.team.setPrefix(parts[0]);
                this.team.setSuffix(parts[1]);
            }
        }
    }
}

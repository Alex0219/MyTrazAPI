package de.fileinputstream.mytraz.knockffa.player;

import org.bukkit.entity.Player;

public class GamePlayer {

    Player player;

    String uuid;

    String name;

    public GamePlayer(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public String getUuid() {
        return uuid;
    }

    public void kickPlayer() {

    }
}

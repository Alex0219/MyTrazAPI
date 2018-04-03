package de.fileinputstream.mytraz.knockffa;

import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;

public class Bootstrap extends JavaPlugin {

    public static Bootstrap instance;

    public Jedis jedis;

    public static Bootstrap getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
    }

    public void buildInstances() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public Jedis getJedis() {
        return jedis;
    }
}

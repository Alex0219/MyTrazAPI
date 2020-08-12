package de.fileinputstream.mytraz.worldmanagement.db;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class RedisConnector {

    public JedisPool jedisPool;

    public RedisConnector() {
    }

    /**
     * Connects to the redis database with host and port.
     *
     * @param host
     * @param port
     */
    public void connectToRedis(String host, int port) {
        jedisPool = new JedisPool(host, port);
        System.out.println("Connected to redis server at " + host + ":" + port + " using password: no");
    }

    /**
     * Connects to the redis database with host, port and password.
     *
     * @param host
     * @param port
     * @param password
     */
    public void connectToRedis(String host, int port, String password) {
        jedisPool = new JedisPool(host, port);
        jedisPool.getResource().auth(password);
        System.out.println("Connected to redis server at " + host + ":" + port + " using password: yes");
    }

    /**
     * Returns a @{@link Jedis} instance from the Jedis pool.
     *
     * @return
     */
    public Jedis getJedis() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis;
        }
    }
}

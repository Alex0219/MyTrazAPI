package de.fileinputstream.redisbuilder.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Cache<K, V> {


    private final Function<K, V> initialValue;
    private final ConcurrentHashMap<K, V> cache;

    public Cache(Function<K, V> initialValue) {
        this.initialValue = initialValue;
        cache = new ConcurrentHashMap<>();
    }

    public V get(K key) {
        synchronized (this) {
            return cache.computeIfAbsent(key, initialValue);
        }
    }

    public synchronized void clean() {
        synchronized (this) {
            cache.clear();
        }
    }
}

package ru.otus.hw.webserver.service.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CacheImpl<K, V> implements Cache<K, V> {
    private static Logger logger = LoggerFactory.getLogger(CacheImpl.class);

    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<Listener> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        cache.remove(key);
        notifyListeners(key, null, "remove");
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);
        notifyListeners(key, value, "get");
        return value;
    }

    @Override
    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String action) {
        for (Listener listener : listeners) {
            if (listener != null) {
                try {
                    listener.notify(key, value, action);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }
}

package ru.otus.hw.webserver.service.cache;

public interface Cache<K, V> {

    void put(K key, V value);

    void remove(K key);

    V get(K key);

    void addListener(Listener listener);

    void removeListener(Listener listener);
}
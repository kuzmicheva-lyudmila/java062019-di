package ru.otus.hw.webserver.dao;

import java.util.List;

public interface Dao<T, S> {
    void create(T objectData);
    void update(T objectData);
    void delete(T objectData);
    <T> T load(S id);
    List<T> loadAll();
}

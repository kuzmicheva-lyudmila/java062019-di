package ru.otus.hw.webserver.service.dbservice;

import ru.otus.hw.webserver.models.User;

import java.util.List;

public interface UserService {
    void create(User objectData);
    void update(User objectData);
    void delete(User objectData);
    User load(long id);
    List<User> loadAll();
}

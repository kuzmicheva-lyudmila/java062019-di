package ru.otus.hw.webserver.service.dbservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.hw.webserver.service.cache.Cache;
import ru.otus.hw.webserver.dao.Dao;
import ru.otus.hw.webserver.models.User;

import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Dao<User, Long> userDao;
    private final Cache<String, User> cache;

    public UserServiceImpl(Cache<String, User> cache, @Qualifier("userDao") Dao<User, Long> userDao) {
        this.userDao = userDao;
        this.cache = cache;

        if (this.cache != null) {
            this.cache.addListener(
                    (key, value, action) -> logger.info("key: {}, value: {}, action: {}", key, value, action)
            );
            for (User user : this.userDao.loadAll()) {
                this.cache.put(String.valueOf(user.getId()), user);
            }
        }
    }

    @Override
    public void create(User objectData) {
        userDao.create(objectData);

        if (this.cache != null) {
            cache.put(String.valueOf(objectData.getId()), objectData);
        }
    }

    @Override
    public void update(User objectData) {
        userDao.update(objectData);

        if (this.cache != null) {
            cache.put(String.valueOf(objectData.getId()), objectData);
        }
    }

    @Override
    public void delete(User objectData) {
        userDao.delete(objectData);

        if (this.cache != null) {
            cache.remove(String.valueOf(objectData.getId()));
        }
    }

    @Override
    public User load(long id) {
        User user = null;

        if (this.cache != null) {
            user = cache.get(String.valueOf(id));
        }

        if (user == null) {
            user = userDao.load(id);
        }

        return user;
    }

    @Override
    public List<User> loadAll() {
        return userDao.loadAll();
    }
}

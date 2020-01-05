package ru.otus.hw.webserver.database.dao.implement;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.otus.hw.webserver.database.HibernateSession;
import ru.otus.hw.webserver.database.dao.UserDao;
import ru.otus.hw.webserver.models.User;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    public UserDaoImpl() {}

    @Override
    public void create(User objectData) {
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.save(objectData);
            transaction.commit();
            logger.info("DB created user: {}", objectData);
        }
    }

    @Override
    public void update(User objectData) {
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.update(objectData);
            transaction.commit();
            logger.info("DB updated user: {}", objectData);
        }
    }

    @Override
    public void delete(User objectData) {
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.delete(objectData);
            transaction.commit();
            logger.info("DB deleted user: {}", objectData);
        }
    }

    @Override
    public User load(Long id) {
        User user;
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            user = session.get(User.class, id);
            logger.info("DB selected user: {}", user);
        }
        return user;
    }

    @Override
    public List<User> loadAll() {
        EntityManager entityManager = HibernateSession.getSessionFactory().createEntityManager();

        return entityManager.createQuery("select u from User u", User.class)
                .getResultList();
    }
}

package ru.otus.hw.webserver.dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw.webserver.dao.handlers.GetUserDataRequestHandler;
import ru.otus.hw.webserver.messagesystem.MessageClient;
import ru.otus.hw.webserver.messagesystem.MessageSystem;
import ru.otus.hw.webserver.messagesystem.MessageType;
import ru.otus.hw.webserver.messagesystem.RequestHandler;
import ru.otus.hw.webserver.models.User;

import javax.persistence.EntityManager;
import java.util.List;

@Component("userDao")
public class UserDaoImpl implements Dao<User, Long> {
    private static Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    private final MessageClient messageClient;

    public UserDaoImpl(
            MessageSystem messageSystem,
            ObjectProvider<MessageClient> objectProvider,
            @Value("${message-client.database.name}") String databaseServiceClientName
    ) {
        this.messageClient = objectProvider.getObject(databaseServiceClientName, messageSystem);
        this.messageClient.addHandler(MessageType.USER_CREATE, new GetUserDataRequestHandler(this));
        this.messageClient.addHandler(MessageType.USER_LIST, new GetUserDataRequestHandler(this));
        messageSystem.addClient(this.messageClient);
    }

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

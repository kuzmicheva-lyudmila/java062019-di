package ru.otus.hw.webserver.database.dao.implement;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.otus.hw.webserver.database.dao.AccountDao;
import ru.otus.hw.webserver.database.HibernateSession;
import ru.otus.hw.webserver.models.Account;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class AccountDaoImpl implements AccountDao {
    public AccountDaoImpl() {
    }

    @Override
    public void create(Account objectData) {
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.save(objectData);
            transaction.commit();
        }
    }

    @Override
    public void update(Account objectData) {
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.update(objectData);
            transaction.commit();
        }
    }

    @Override
    public void delete(Account objectData) {
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            Transaction transaction = session.getTransaction();
            transaction.begin();
            session.delete(objectData);
            transaction.commit();
        }
    }

    @Override
    public List<Account> loadAll() {
        EntityManager entityManager = HibernateSession.getSessionFactory().createEntityManager();
        return entityManager.createQuery("select a from Account a", Account.class)
                .getResultList();
    }

    @Override
    public Account load(String id) {
        try (Session session = HibernateSession.getSessionFactory().openSession()) {
            return session.get(Account.class, id);
        }
    }
}

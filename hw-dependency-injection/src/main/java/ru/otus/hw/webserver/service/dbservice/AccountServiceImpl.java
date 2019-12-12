package ru.otus.hw.webserver.service.dbservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.otus.hw.webserver.dao.Dao;
import ru.otus.hw.webserver.models.Account;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

@Service
public class AccountServiceImpl implements AccountService {
    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final Dao<Account, String> accountDao;

    public AccountServiceImpl(@Qualifier("accountDao") Dao<Account, String> accountDao) {
        this.accountDao = accountDao;
        createAdminAccount();
    }

    @Override
    public void create(String login, String password) {
        accountDao.create(new Account(
                login,
                Account.passwordHash(password)
            )
        );
    }

    @Override
    public void updatePassword(Account objectData, String newPassword) {
        objectData.setPassword(Account.passwordHash(newPassword));
        accountDao.update(objectData);
    }

    @Override
    public void delete(Account objectData) {
        accountDao.delete(objectData);
    }

    @Override
    public Account load(String account) {
        return (Account) accountDao.load(account);
    }

    @Override
    public List<Account> loadAll() {
        return accountDao.loadAll();
    }

    @Override
    public Account loginWithAccount(Account account) {
        Account dbAccount = load(account.getLogin());
        if (dbAccount != null
                && !account.getLogin().isEmpty()
                && !account.getPassword().isEmpty()
                && !dbAccount.getPassword().equals(Account.passwordHash(account.getPassword()))) {
            return dbAccount;
        }

        return null;
    }

    private void createAdminAccount() {
        String login = null;
        String password = null;
        Properties settings = new Properties();
        try {
            settings.load(getClass().getResourceAsStream("/application.properties"));
            login = settings.getProperty("admin.login", "admin1");
            password = settings.getProperty("admin.password", "admin1");
        } catch (IOException e) {
            logger.error("error on reading properties: [" + e.getMessage() + "]");
        }
        accountDao.create(new Account(login, password));
    }
}

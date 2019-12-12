package ru.otus.hw.webserver.service.dbservice;

import ru.otus.hw.webserver.models.Account;

import java.util.List;

public interface AccountService {
    void create(String login, String password);
    void updatePassword(Account objectData, String newPassword);
    void delete(Account objectData);
    Account load(String id);
    List<Account> loadAll();

    Account loginWithAccount(Account account);
}

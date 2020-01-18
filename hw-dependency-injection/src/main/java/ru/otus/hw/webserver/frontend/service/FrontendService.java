package ru.otus.hw.webserver.frontend.service;

import ru.otus.hw.messageserver.Message;
import ru.otus.hw.messageserver.SocketService;
import ru.otus.hw.webserver.frontend.controllers.Person;

import java.io.IOException;

public interface FrontendService extends SocketService {
    void createUser(Person person) throws IOException;

    void loadAll() throws IOException;

    @Override
    boolean getMessage(Message message);
}

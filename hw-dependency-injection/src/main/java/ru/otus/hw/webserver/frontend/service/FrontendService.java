package ru.otus.hw.webserver.frontend.service;

import ru.otus.hw.webserver.frontend.controllers.Person;
import ru.otus.hw.webserver.frontend.sockets.Message;

import java.io.IOException;

public interface FrontendService {
    void createUser(Person person) throws IOException;

    void loadAll() throws IOException;

    void takeMessage(Message message);
}

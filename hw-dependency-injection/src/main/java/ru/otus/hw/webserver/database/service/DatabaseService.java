package ru.otus.hw.webserver.database.service;

import ru.otus.hw.webserver.database.models.Person;
import ru.otus.hw.webserver.database.sockets.Message;

import java.io.IOException;
import java.util.List;

public interface DatabaseService {
    void takeMessage(Message message) throws IOException;
}

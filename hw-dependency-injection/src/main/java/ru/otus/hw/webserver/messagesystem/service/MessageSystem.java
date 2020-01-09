package ru.otus.hw.webserver.messagesystem.service;

import ru.otus.hw.webserver.messagesystem.sockets.Message;

public interface MessageSystem {

    String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    void addClient(MessageClient msClient);
    void removeClient(String clientId, String host, int port);

    boolean newMessage(Message msg);
    void dispose() throws InterruptedException;
}

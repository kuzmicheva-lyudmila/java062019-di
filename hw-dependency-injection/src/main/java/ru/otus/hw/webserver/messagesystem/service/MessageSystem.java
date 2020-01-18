package ru.otus.hw.webserver.messagesystem.service;

import ru.otus.hw.messageserver.Message;
import ru.otus.hw.messageserver.SocketService;

public interface MessageSystem extends SocketService {

    String BACKEND_SERVICE_CLIENT_NAME = "backendService";

    void addClient(MessageClient msClient);
    void removeClient(String clientId, String host, int port);
    void dispose() throws InterruptedException;

    @Override
    boolean getMessage(Message msg);
}

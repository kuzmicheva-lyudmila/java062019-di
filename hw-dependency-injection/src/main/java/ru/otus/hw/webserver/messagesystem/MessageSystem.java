package ru.otus.hw.webserver.messagesystem;

public interface MessageSystem {

    String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    void addClient(MessageClient msClient);
    void removeClient(String clientId);
    boolean newMessage(Message msg);
    void dispose() throws InterruptedException;
}

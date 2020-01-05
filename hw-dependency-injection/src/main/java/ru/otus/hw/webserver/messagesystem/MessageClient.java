package ru.otus.hw.webserver.messagesystem;

public interface MessageClient {
    void addHandler(MessageType type, Handler handler);

    boolean sendMessage(Message msg);

    void handle(Message msg);

    String getName();

    <T> Message produceMessage(String to, T data, MessageType msgType);

}

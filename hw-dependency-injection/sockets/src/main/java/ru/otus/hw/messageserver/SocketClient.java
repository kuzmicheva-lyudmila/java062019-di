package ru.otus.hw.messageserver;

public interface SocketClient {
    void sendMessage(Message message);

    void sendMessageToURL(Message message, SocketURL socketURL);
}


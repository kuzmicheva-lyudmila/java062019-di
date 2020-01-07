package ru.otus.hw.webserver.database.sockets;

import java.io.IOException;

public interface SocketClient {
    void sendMessage(Message message) throws IOException;
}

package ru.otus.hw.webserver.frontend.sockets;

import java.io.IOException;

public interface SocketClient {
    void sendMessage(Message message) throws IOException;
}

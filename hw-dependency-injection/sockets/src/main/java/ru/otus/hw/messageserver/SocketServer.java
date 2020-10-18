package ru.otus.hw.messageserver;

import java.io.IOException;
import java.net.Socket;

public interface SocketServer {
    void go();
    void clientHandler(Socket clientSocket) throws IOException;
}

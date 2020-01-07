package ru.otus.hw.webserver.database.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public interface SocketServer {
    void go();

    void clientHandler(Socket clientSocket) throws IOException;
}

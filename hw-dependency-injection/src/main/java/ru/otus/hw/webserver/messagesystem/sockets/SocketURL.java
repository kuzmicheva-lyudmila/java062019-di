package ru.otus.hw.webserver.messagesystem.sockets;

import lombok.Data;

@Data
public class SocketURL {
    private final String host;
    private final int port;
    private final String clientName;
}

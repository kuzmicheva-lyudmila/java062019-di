package ru.otus.hw.webserver.messagesystem.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.hw.messageserver.SocketURL;

import java.util.Objects;

public class MessageClient {
    private static final Logger logger = LoggerFactory.getLogger(MessageClient.class);

    private final SocketURL socketURL;

    public MessageClient(
            String name,
            String host,
            int port
    ) {
        this.socketURL = new SocketURL(host, port, name);
    }

    public String getName() {
        return socketURL.getClientName();
    }

    public SocketURL getSocketURL() {
        return socketURL;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
       if (o == null || getClass() != o.getClass()) return false;
        MessageClient msClient = (MessageClient) o;
        return Objects.equals(socketURL.getClientName(), msClient.getSocketURL().getClientName())
                && Objects.equals(socketURL.getHost(), msClient.getSocketURL().getHost())
                && (socketURL.getPort() == msClient.socketURL.getPort());
    }

    @Override
    public int hashCode() {
        return Objects.hash(socketURL.getClientName(), socketURL.getHost(), socketURL.getPort());
    }
}


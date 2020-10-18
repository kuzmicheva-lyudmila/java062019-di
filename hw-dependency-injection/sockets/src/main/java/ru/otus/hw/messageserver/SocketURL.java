package ru.otus.hw.messageserver;

public class SocketURL {
    private final String host;
    private final int port;
    private final String clientName;

    public SocketURL(String host, int port, String clientName) {
        this.host = host;
        this.port = port;
        this.clientName = clientName;
    }

    public String getHost() {
        return this.host;
    }

    public int getPort() {
        return this.port;
    }

    public String getClientName() {
        return this.clientName;
    }
}


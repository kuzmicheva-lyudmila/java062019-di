package ru.otus.hw.webserver.frontend.sockets;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Message {
    private final String clientName;
    private final String host;
    private final int port;

    private final String type;
    private final String parameters;
}

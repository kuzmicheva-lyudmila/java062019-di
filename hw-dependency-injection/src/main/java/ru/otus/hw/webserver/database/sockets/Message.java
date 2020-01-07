package ru.otus.hw.webserver.database.sockets;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Message {
    private final String clientName;
    private final String host;
    private final int port;

    private final String type;
    private String parameters;
}

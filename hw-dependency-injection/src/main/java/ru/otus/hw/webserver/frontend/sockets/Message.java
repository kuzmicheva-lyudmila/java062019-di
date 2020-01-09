package ru.otus.hw.webserver.frontend.sockets;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String clientName;
    private String host;
    private int port;
    private String toClientName;
    private String toHost;
    private int toPort;

    private String type;
    private String parameters;
}

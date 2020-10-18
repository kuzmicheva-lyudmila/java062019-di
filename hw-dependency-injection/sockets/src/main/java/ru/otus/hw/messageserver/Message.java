package ru.otus.hw.messageserver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

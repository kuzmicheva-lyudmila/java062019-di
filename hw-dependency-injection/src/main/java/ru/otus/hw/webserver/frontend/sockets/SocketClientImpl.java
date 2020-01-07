package ru.otus.hw.webserver.frontend.sockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

@Component
public class SocketClientImpl implements SocketClient {
    private static Logger logger = LoggerFactory.getLogger(SocketClientImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final SocketURL messageSystemURL;
    private final SocketURL frontendURL;
    private final String messageClientName;

    public SocketClientImpl(
            @Value("${messagesystem.host}") String messageSystemHost,
            @Value("${messagesystem.port}") int messageSystemPort,
            @Value("${frontend.host}") String frontendHost,
            @Value("${frontend.port}") int frontendPort,
            @Value("${message-client.frontend.name}") String messageClientName
    ) {
        this.messageSystemURL = new SocketURL(messageSystemHost, messageSystemPort);
        this.frontendURL = new SocketURL(frontendHost, frontendPort);
        this.messageClientName = messageClientName;
    }

    @Override
    public void sendMessage(Message message) {
        try {
            try (Socket clientSocket = new Socket(messageSystemURL.getHost(), messageSystemURL.getPort())) {
                String jsonMessage = mapper.writeValueAsString(message);
                logger.info("to messagesystem server: {} ", jsonMessage);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(jsonMessage);
            }
        } catch (Exception ex) {
            logger.error("frontend -> sendMessage error", ex);
        }
    }

    @Override
    public String getMessageClientName() {
        return messageClientName;
    }

    @Override
    public SocketURL getFrontendURL() {
        return frontendURL;
    }
}
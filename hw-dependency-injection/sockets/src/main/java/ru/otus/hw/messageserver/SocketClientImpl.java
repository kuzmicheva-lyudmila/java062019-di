package ru.otus.hw.messageserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.net.Socket;

public class SocketClientImpl implements SocketClient {
    private static Logger logger = LoggerFactory.getLogger(SocketClientImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final String clientName;

    public SocketClientImpl(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public void sendMessage(Message message) {
        sendMessage(
                message,
                new SocketURL(
                    message.getToHost(),
                    message.getToPort(),
                    message.getToClientName()
                )
        );
    }

    @Override
    public void sendMessageToURL(Message message, SocketURL socketURL) {
        sendMessage(message, socketURL);
    }

    private void sendMessage(Message message, SocketURL socketURL) {
        try {
            try (Socket clientSocket = new Socket(socketURL.getHost(), socketURL.getPort())) {
                String jsonMessage = mapper.writeValueAsString(message);
                logger.info("{}: {} ", clientName, jsonMessage);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(jsonMessage);
            }
        } catch (Exception ex) {
            logger.error("{}} -> sendMessage error", clientName, ex);
        }
    }
}

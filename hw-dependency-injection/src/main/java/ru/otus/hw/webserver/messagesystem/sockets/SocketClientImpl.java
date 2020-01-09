package ru.otus.hw.webserver.messagesystem.sockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.net.Socket;

@Component
public class SocketClientImpl implements SocketClient {
    private static Logger logger = LoggerFactory.getLogger(SocketClientImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();

    public SocketClientImpl() {
    }

    @Override
    public void sendMessage(Message message) {
        try {
            try (Socket clientSocket = new Socket(message.getToHost(), message.getToPort())) {
                String jsonMessage = mapper.writeValueAsString(message);
                logger.info("from messagesystem client: {} ", jsonMessage);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(jsonMessage);
            }
        } catch (Exception ex) {
            logger.error("messageSystem -> sendMessage error {}", message, ex);
        }
    }
}

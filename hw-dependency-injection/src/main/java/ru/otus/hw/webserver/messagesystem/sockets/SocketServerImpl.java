package ru.otus.hw.webserver.messagesystem.sockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw.webserver.messagesystem.service.MessageSystem;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SocketServerImpl implements SocketServer {
    private static Logger logger = LoggerFactory.getLogger(SocketServerImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final int messageSystemPort;
    private final MessageSystem messageSystem;

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("messagesystem-socket-server");
        return thread;
    });

    public SocketServerImpl(
            @Value("${messagesystem.port}") int messageSystemPort,
            MessageSystem messageSystem
    ) {
        this.messageSystemPort = messageSystemPort;
        this.messageSystem = messageSystem;
        msgProcessor.submit(this::go);
    }

    @Override
    public void go() {
        try (ServerSocket serverSocket = new ServerSocket(messageSystemPort)) {
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("MessageSystemServer: waiting for client connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    clientHandler(clientSocket);
                }
            }
        } catch (Exception ex) {
            logger.error("messagesystemServer: error", ex);
        }
    }

    @Override
    public void clientHandler(Socket clientSocket) throws IOException {
        Message message = mapper.readValue(clientSocket.getInputStream(), Message.class);
        if (message != null) {
            messageSystem.newMessage(message);
        }
    }
}

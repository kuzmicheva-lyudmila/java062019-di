package ru.otus.hw.webserver.frontend.sockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw.webserver.frontend.service.FrontendService;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SocketServerImpl implements SocketServer {
    private static Logger logger = LoggerFactory.getLogger(SocketServerImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private static String MESSAGE_TYPE_REGISTER_CLIENT = "RegisterClient";

    private final int frontendPort;

    private final FrontendService frontendService;

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("frontend-socket-server");
        return thread;
    });

    public SocketServerImpl(
            @Value("${frontend.host}") String frontendHost,
            @Value("${frontend.port}") int frontendPort,
            @Value("${message-client.frontend.name}") String messageClientName,
            FrontendService frontendService,
            SocketClient socketClient
    ) throws IOException {
        this.frontendPort = frontendPort;
        this.frontendService = frontendService;
        Message message = new Message(
                messageClientName,
                frontendHost,
                frontendPort,
                "",
                "",
                0,
                MESSAGE_TYPE_REGISTER_CLIENT,
                ""
        );
        socketClient.sendMessage(message);
        msgProcessor.submit(this::go);
    }

    @Override
    public void go() {
        try (ServerSocket serverSocket = new ServerSocket(frontendPort)) {
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("frontendServer: waiting for client connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    clientHandler(clientSocket);
                }
            }
        } catch (Exception ex) {
            logger.error("frontendServer: error", ex);
        }
    }

    @Override
    public void clientHandler(Socket clientSocket) throws IOException {
        Message message = mapper.readValue(clientSocket.getInputStream(), Message.class);
        if (message != null) {
            frontendService.takeMessage(message);
        }
    }
}

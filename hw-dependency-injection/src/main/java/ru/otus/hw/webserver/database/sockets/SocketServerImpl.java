package ru.otus.hw.webserver.database.sockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw.webserver.database.service.DatabaseService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class SocketServerImpl implements SocketServer {
    private static Logger logger = LoggerFactory.getLogger(SocketServerImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final int databasePort;

    private final DatabaseService databaseService;

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> {
        Thread thread = new Thread(runnable);
        thread.setName("database-socket-server");
        return thread;
    });

    public SocketServerImpl(
            @Value("${database.port}") int databasePort,
            DatabaseService databaseService
    ) {
        this.databasePort = databasePort;
        this.databaseService = databaseService;
        msgProcessor.submit(this::go);
    }

    @Override
    public void go() {
        try (ServerSocket serverSocket = new ServerSocket(databasePort)) {
            while (!Thread.currentThread().isInterrupted()) {
                SocketServerImpl.logger.info("databaseServer: waiting for client connection");
                try (Socket clientSocket = serverSocket.accept()) {
                    clientHandler(clientSocket);
                }
            }
        } catch (Exception ex) {
            SocketServerImpl.logger.error("databaseServer: error", ex);
        }
    }

    @Override
    public void clientHandler(Socket clientSocket) throws IOException {
        Message message = mapper.readValue(clientSocket.getInputStream(), Message.class);
        if (message != null) {
            databaseService.takeMessage(message);
        }
    }
}

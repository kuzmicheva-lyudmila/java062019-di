package ru.otus.hw.messageserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServerImpl implements SocketServer {
    private static Logger logger = LoggerFactory.getLogger(SocketServerImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final String systemName;
    private final int systemPort;
    private final SocketService service;

    private final ExecutorService msgProcessor = Executors.newSingleThreadExecutor(runnable -> new Thread(runnable));

    public SocketServerImpl(
            String systemName,
            int systemPort,
            SocketService service
    ) {
        this.systemName = systemName;
        this.systemPort = systemPort;
        this.service = service;
        msgProcessor.submit(this::go);
    }

    @Override
    public void go() {
        try (ServerSocket serverSocket = new ServerSocket(systemPort)) {
            while (!Thread.currentThread().isInterrupted()) {
                logger.info("{}: waiting for client connection", systemName);
                try (Socket clientSocket = serverSocket.accept()) {
                    clientHandler(clientSocket);
                } catch (Exception ex) {
                    logger.error("{}: error", systemName, ex);
                }
            }
        } catch (Exception ex) {
            logger.error("{}: error", systemName, ex);
        }
    }

    @Override
    public void clientHandler(Socket clientSocket) throws IOException {
        Message message = mapper.readValue(clientSocket.getInputStream(), Message.class);
        if (message != null) {
            service.getMessage(message);
        }
    }

}

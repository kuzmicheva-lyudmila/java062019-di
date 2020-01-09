package ru.otus.hw.webserver.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.webserver.frontend.controllers.Person;
import ru.otus.hw.webserver.frontend.sockets.Message;
import ru.otus.hw.webserver.frontend.sockets.SocketClient;
import ru.otus.hw.webserver.frontend.sockets.SocketURL;

import java.io.IOException;

@Service
public class FrontendServiceImpl implements FrontendService {
    private static String MESSAGE_TYPE_USER_LIST = "UserList";
    private static String MESSAGE_TYPE_USER_CREATE = "UserCreate";

    private static Logger logger = LoggerFactory.getLogger(FrontendServiceImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final SocketClient socketClient;
    private final SimpMessagingTemplate template;
    private final String databaseClientName;
    private final SocketURL frontendURL;

    public FrontendServiceImpl(
            SocketClient socketClient,
            SimpMessagingTemplate template,
            @Value("${frontend.host}") String frontendHost,
            @Value("${frontend.port}") int frontendPort,
            @Value("${message-client.frontend.name}") String messageClientName,
            @Value("${message-client.database.name}") String databaseClientName
    ) {
        this.socketClient = socketClient;
        this.template = template;
        this.databaseClientName = databaseClientName;
        this.frontendURL = new SocketURL(frontendHost, frontendPort, messageClientName);
    }

    @Override
    public void createUser(Person person) throws IOException {
        Message message = new Message(
                frontendURL.getClientName(),
                frontendURL.getHost(),
                frontendURL.getPort(),
                databaseClientName,
                "",
                0,
                MESSAGE_TYPE_USER_CREATE,
                mapper.writeValueAsString(person)
        );
        socketClient.sendMessage(message);
    }

    @Override
    public void loadAll() throws IOException {
        Message message = new Message(
                frontendURL.getClientName(),
                frontendURL.getHost(),
                frontendURL.getPort(),
                databaseClientName,
                "",
                0,
                MESSAGE_TYPE_USER_LIST,
                ""
        );
        socketClient.sendMessage(message);
    }

    @Override
    public void takeMessage(Message message) {
        if (message.getType().equals(MESSAGE_TYPE_USER_CREATE)) {
            logger.info("the user created success");
        } else if (message.getType().equals(MESSAGE_TYPE_USER_LIST)) {
            template.convertAndSend("/topic/users", message.getParameters());
        } else {
            logger.error("frontend -> take message: unknown message type {}", message.toString());
        }
    }
}

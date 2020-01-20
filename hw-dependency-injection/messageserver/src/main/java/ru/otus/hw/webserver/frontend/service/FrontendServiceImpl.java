package ru.otus.hw.webserver.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.messageserver.Message;
import ru.otus.hw.messageserver.SocketClient;
import ru.otus.hw.messageserver.SocketURL;
import ru.otus.hw.webserver.frontend.controllers.Person;

import java.io.IOException;

@Service
public class FrontendServiceImpl implements FrontendService {
    private static final String MESSAGE_TYPE_USER_LIST = "UserList";
    private static final String MESSAGE_TYPE_USER_CREATE = "UserCreate";
    private static final String MESSAGE_TYPE_REGISTER_CLIENT = "RegisterClient";

    private static Logger logger = LoggerFactory.getLogger(FrontendServiceImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final SocketClient socketClient;
    private final SimpMessagingTemplate template;
    private final String backendClientName;
    private final SocketURL frontendURL;
    private final SocketURL messageSystemURL;

    public FrontendServiceImpl(
            SocketURL frontendURL,
            SocketURL messageSystemURL,
            SocketClient socketClient,
            SimpMessagingTemplate template,
            @Value("${message-client.backend.name}") String backendClientName
    ) {
        this.socketClient = socketClient;
        this.template = template;
        this.backendClientName = backendClientName;
        this.frontendURL = frontendURL;
        this.messageSystemURL = messageSystemURL;

        Message message = new Message(
                frontendURL.getClientName(),
                frontendURL.getHost(),
                frontendURL.getPort(),
                "",
                "",
                0,
                MESSAGE_TYPE_REGISTER_CLIENT,
                ""
        );
        socketClient.sendMessageToURL(message, messageSystemURL);
    }

    @Override
    public void createUser(Person person) throws IOException {
        Message message = new Message(
                frontendURL.getClientName(),
                frontendURL.getHost(),
                frontendURL.getPort(),
                backendClientName,
                "",
                0,
                MESSAGE_TYPE_USER_CREATE,
                mapper.writeValueAsString(person)
        );
        socketClient.sendMessageToURL(message, messageSystemURL);
    }

    @Override
    public void loadAll() throws IOException {
        Message message = new Message(
                frontendURL.getClientName(),
                frontendURL.getHost(),
                frontendURL.getPort(),
                backendClientName,
                "",
                0,
                MESSAGE_TYPE_USER_LIST,
                ""
        );
        socketClient.sendMessageToURL(message, messageSystemURL);
    }

    @Override
    public boolean getMessage(Message message) {
        if (message.getType().equals(MESSAGE_TYPE_USER_CREATE)) {
            logger.info("the user created success");
            return true;
        } else if (message.getType().equals(MESSAGE_TYPE_USER_LIST)) {
            template.convertAndSend("/topic/users", message.getParameters());
            return true;
        } else {
            logger.error("frontend -> take message: unknown message type {}", message.toString());
            return false;
        }
    }
}

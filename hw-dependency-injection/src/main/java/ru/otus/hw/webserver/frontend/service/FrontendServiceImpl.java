package ru.otus.hw.webserver.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.webserver.frontend.controllers.Person;
import ru.otus.hw.webserver.frontend.sockets.Message;
import ru.otus.hw.webserver.frontend.sockets.SocketClient;

import java.io.IOException;

@Service
public class FrontendServiceImpl implements FrontendService {
    private static String MESSAGE_TYPE_USER_LIST = "UserList";
    private static String MESSAGE_TYPE_USER_CREATE = "UserCreate";

    private static Logger logger = LoggerFactory.getLogger(FrontendServiceImpl.class);
    private final ObjectMapper mapper = new ObjectMapper();

    private final SocketClient socketClient;
    private final SimpMessagingTemplate template;

    public FrontendServiceImpl(
            SocketClient socketClient,
            SimpMessagingTemplate template
    ) {
        this.socketClient = socketClient;
        this.template = template;
    }

    @Override
    public void createUser(Person person) throws IOException {
        Message message = Message.builder()
                .clientName(socketClient.getMessageClientName())
                .host(socketClient.getFrontendURL().getHost())
                .port(socketClient.getFrontendURL().getPort())
                .type(MESSAGE_TYPE_USER_CREATE)
                .parameters(mapper.writeValueAsString(person))
                .build();
        socketClient.sendMessage(message);
    }

    @Override
    public void loadAll() throws IOException {
        Message message = Message.builder()
                .clientName(socketClient.getMessageClientName())
                .host(socketClient.getFrontendURL().getHost())
                .port(socketClient.getFrontendURL().getPort())
                .type(MESSAGE_TYPE_USER_LIST)
                .build();
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

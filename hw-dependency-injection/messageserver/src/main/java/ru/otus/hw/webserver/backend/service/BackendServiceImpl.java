package ru.otus.hw.webserver.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.hw.messageserver.Message;
import ru.otus.hw.messageserver.SocketClient;
import ru.otus.hw.messageserver.SocketURL;
import ru.otus.hw.webserver.backend.dao.PersonRepository;
import ru.otus.hw.webserver.backend.models.Person;

@Service
public class BackendServiceImpl implements BackendService {
    private static Logger logger = LoggerFactory.getLogger(BackendServiceImpl.class);

    private static final String MESSAGE_TYPE_USER_LIST = "UserList";
    private static final String MESSAGE_TYPE_USER_CREATE = "UserCreate";
    private static final String MESSAGE_TYPE_REGISTER_CLIENT = "RegisterClient";
    private final ObjectMapper mapper = new ObjectMapper();

    private final PersonRepository personRepository;
    private final SocketClient backendClient;
    private final SocketURL backendURL;
    private final SocketURL messageSystemURL;

    public BackendServiceImpl(
            PersonRepository personRepository,
            SocketClient backendClient,
            SocketURL backendURL,
            SocketURL messageSystemURL
    ) {
        this.personRepository = personRepository;
        this.backendClient = backendClient;
        this.backendURL = backendURL;
        this.messageSystemURL = messageSystemURL;

        Message message = new Message(
                backendURL.getClientName(),
                backendURL.getHost(),
                backendURL.getPort(),
                "",
                "",
                0,
                MESSAGE_TYPE_REGISTER_CLIENT,
                ""
        );
        backendClient.sendMessageToURL(message, messageSystemURL);
    }

    @Override
    public boolean getMessage(Message message) {
        if (message.getType().equals(MESSAGE_TYPE_USER_CREATE)) {
            Person person = null;
            try {
                person = mapper.readValue(message.getParameters(), Person.class);
            } catch (JsonProcessingException e) {
                logger.error("backend -> take message: error on parse message {}", message.toString());
                return false;
            }
            personRepository.save(person);
            backendClient.sendMessageToURL(
                    new Message(
                        backendURL.getClientName(),
                        backendURL.getHost(),
                        backendURL.getPort(),
                        message.getClientName(),
                        message.getHost(),
                        message.getPort(),
                        message.getType(),
                        message.getParameters()
                      ),
                    messageSystemURL
            );
            return true;
        } else if (message.getType().equals(MESSAGE_TYPE_USER_LIST)) {
            String parameters = personRepository.findAll().toString();
            backendClient.sendMessageToURL(
                    new Message(
                            backendURL.getClientName(),
                            backendURL.getHost(),
                            backendURL.getPort(),
                            message.getClientName(),
                            message.getHost(),
                            message.getPort(),
                            message.getType(),
                            parameters
                    ),
                    messageSystemURL
            );
            return true;
        } else {
            logger.error("backend -> take message: unknown message type {}", message.toString());
            return false;
        }
    }
}

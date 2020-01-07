package ru.otus.hw.webserver.database.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.hw.webserver.database.dao.PersonRepository;
import ru.otus.hw.webserver.database.models.Person;
import ru.otus.hw.webserver.database.sockets.Message;
import ru.otus.hw.webserver.database.sockets.SocketClient;

import java.io.IOException;

@Service
public class DatabaseServiceImpl implements DatabaseService {
    private static Logger logger = LoggerFactory.getLogger(DatabaseServiceImpl.class);

    private static String MESSAGE_TYPE_USER_LIST = "UserList";
    private static String MESSAGE_TYPE_USER_CREATE = "UserCreate";
    private final ObjectMapper mapper = new ObjectMapper();

    private final PersonRepository personRepository;
    private final SocketClient socketClient;

    public DatabaseServiceImpl(
            PersonRepository personRepository,
            SocketClient socketClient
    ) {
        this.personRepository = personRepository;
        this.socketClient = socketClient;
    }

    @Override
    public void takeMessage(Message message) throws IOException {
        if (message.getType().equals(MESSAGE_TYPE_USER_CREATE)) {
            Person person = mapper.readValue(message.getParameters(), Person.class);
            personRepository.save(person);
            socketClient.sendMessage(message);
        } else if (message.getType().equals(MESSAGE_TYPE_USER_LIST)) {
            message.setParameters(personRepository.findAll().toString());
            socketClient.sendMessage(message);
        } else {
            logger.error("database -> take message: unknown message type {}", message.toString());
        }
    }
}

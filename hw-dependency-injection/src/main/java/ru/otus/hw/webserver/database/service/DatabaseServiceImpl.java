package ru.otus.hw.webserver.database.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.hw.webserver.database.dao.PersonRepository;
import ru.otus.hw.webserver.database.models.Person;
import ru.otus.hw.webserver.database.sockets.Message;
import ru.otus.hw.webserver.database.sockets.SocketClient;
import ru.otus.hw.webserver.database.sockets.SocketURL;

import java.io.IOException;

@Service
public class DatabaseServiceImpl implements DatabaseService {
    private static Logger logger = LoggerFactory.getLogger(DatabaseServiceImpl.class);

    private static String MESSAGE_TYPE_USER_LIST = "UserList";
    private static String MESSAGE_TYPE_USER_CREATE = "UserCreate";
    private final ObjectMapper mapper = new ObjectMapper();

    private final PersonRepository personRepository;
    private final SocketClient socketClient;
    private final SocketURL databaseURL;

    public DatabaseServiceImpl(
            PersonRepository personRepository,
            SocketClient socketClient,
            @Value("${database.host}") String databaseHost,
            @Value("${database.port}") int databasePort,
            @Value("${message-client.database.name}") String databaseClientName
    ) {
        this.personRepository = personRepository;
        this.socketClient = socketClient;
        this.databaseURL = new SocketURL(databaseHost, databasePort, databaseClientName);
    }

    @Override
    public void takeMessage(Message message) throws IOException {
        if (message.getType().equals(MESSAGE_TYPE_USER_CREATE)) {
            Person person = mapper.readValue(message.getParameters(), Person.class);
            personRepository.save(person);
            socketClient.sendMessage(
                    new Message(
                        databaseURL.getClientName(),
                        databaseURL.getHost(),
                        databaseURL.getPort(),
                        message.getClientName(),
                        message.getHost(),
                        message.getPort(),
                        message.getType(),
                        message.getParameters()
                      )
            );
        } else if (message.getType().equals(MESSAGE_TYPE_USER_LIST)) {
            String parameters = personRepository.findAll().toString();
            socketClient.sendMessage(
                    new Message(
                            databaseURL.getClientName(),
                            databaseURL.getHost(),
                            databaseURL.getPort(),
                            message.getClientName(),
                            message.getHost(),
                            message.getPort(),
                            message.getType(),
                            parameters
                    )
            );
        } else {
            logger.error("database -> take message: unknown message type {}", message.toString());
        }
    }
}

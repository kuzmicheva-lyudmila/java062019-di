package ru.otus.hw.webserver.database;

import org.springframework.stereotype.Service;
import ru.otus.hw.webserver.database.dao.UserDao;
import ru.otus.hw.webserver.database.handlers.GetUserDataRequestHandler;
import ru.otus.hw.webserver.messagesystem.*;
import ru.otus.hw.webserver.messagesystem.MessageClientImpl;

@Service
public class DatabaseServiceImpl implements DatabaseService {

    private final MessageClient messageClient;

    public DatabaseServiceImpl(
            UserDao userDao,
            MessageClient databaseMessageClient
    ) {
        RequestHandler requestHandler = new GetUserDataRequestHandler(userDao);
        this.messageClient = databaseMessageClient;
        this.messageClient.addHandler(MessageType.USER_CREATE, requestHandler);
        this.messageClient.addHandler(MessageType.USER_LIST, requestHandler);
    }
}

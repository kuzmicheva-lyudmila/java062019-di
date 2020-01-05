package ru.otus.hw.webserver.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.otus.hw.webserver.messagesystem.*;
import ru.otus.hw.webserver.messagesystem.MessageClientImpl;

@Component
public class DBMessageClientImpl implements DBMessageClient {

    public DBMessageClientImpl(
            MessageSystem messageSystem,
            RequestHandler requestHandler,
            @Value("${message-client.database.name}") String databaseServiceClientName
    ) {
        MessageClient messageClient = new MessageClientImpl(databaseServiceClientName, messageSystem);
        messageClient.addHandler(MessageType.USER_CREATE, requestHandler);
        messageClient.addHandler(MessageType.USER_LIST, requestHandler);
        messageSystem.addClient(messageClient);
    }
}

package ru.otus.hw.webserver.messagesystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.webserver.database.dao.UserDao;
import ru.otus.hw.webserver.database.handlers.GetUserDataRequestHandler;
import ru.otus.hw.webserver.frontend.UserService;
import ru.otus.hw.webserver.frontend.handlers.GetUserDataResponseHandler;

@Configuration
public class MessageSystemConfig {
    @Value("${message-client.user.name}")
    String userServiceClientName;

    @Value("${message-client.database.name}")
    String databaseServiceClientName;

    @Autowired
    MessageSystem messageSystem;

    @Bean
    MessageClient frontendMessageClient(UserService userService) {
        MessageClient messageClient = new MessageClientImpl(userServiceClientName, messageSystem);
        ResponseHandler responseHandler = new GetUserDataResponseHandler(userService);
        messageClient.addHandler(MessageType.USER_CREATE, responseHandler);
        messageClient.addHandler(MessageType.USER_LIST, responseHandler);
        messageSystem.addClient(messageClient);
        return messageClient;
    }

    @Bean
    MessageClient databaseMessageClient(UserDao userDao) {
        MessageClient messageClient = new MessageClientImpl(databaseServiceClientName, messageSystem);
        RequestHandler requestHandler = new GetUserDataRequestHandler(userDao);
        messageClient.addHandler(MessageType.USER_CREATE, requestHandler);
        messageClient.addHandler(MessageType.USER_LIST, requestHandler);
        messageSystem.addClient(messageClient);
        return messageClient;
    }
}

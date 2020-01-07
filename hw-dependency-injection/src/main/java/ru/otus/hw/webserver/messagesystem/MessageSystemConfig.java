package ru.otus.hw.webserver.messagesystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageSystemConfig {
    @Value("${message-client.user.name}")
    String userServiceClientName;

    @Value("${message-client.database.name}")
    String databaseServiceClientName;

    @Autowired
    MessageSystem messageSystem;

    @Bean
    MessageClient frontendMessageClient() {
        MessageClient messageClient = new MessageClientImpl(userServiceClientName, messageSystem);
        messageSystem.addClient(messageClient);
        return messageClient;
    }

    @Bean
    MessageClient databaseMessageClient() {
        MessageClient messageClient = new MessageClientImpl(databaseServiceClientName, messageSystem);
        messageSystem.addClient(messageClient);
        return messageClient;
    }
}

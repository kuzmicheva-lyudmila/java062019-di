package ru.otus.hw.webserver.messagesystem;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.messageserver.*;

@Configuration
public class MessageSystemConfig {
    @Value("${message-client.messagesystem.name}")
    String messageSystemName;

    @Value("${messagesystem.port}")
    int messageSystemPort;

    @Bean
    SocketClient messageSystemClient() {
        return new SocketClientImpl(messageSystemName);
    }

    @Bean
    SocketServer messageSystemServer(SocketService messageSystem) {
        return new SocketServerImpl(messageSystemName, messageSystemPort, messageSystem);
    }
}

package ru.otus.hw.webserver.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.messageserver.*;

@Configuration
public class BackendConfig {
    @Value("${backend.host}")
    String backendHost;

    @Value("${backend.port}")
    int backendPort;

    @Value("${message-client.backend.name}")
    String backendClientName;

    @Value("${messagesystem.host}")
    String messageSystemHost;

    @Value("${messagesystem.port}")
    int messageSystemPort;

    @Bean
    SocketURL backendURL() {
        return new SocketURL(backendHost, backendPort, backendClientName);
    }

    @Bean
    SocketURL messageSystemURL() {
        return new SocketURL(messageSystemHost, messageSystemPort, "");
    }

    @Bean
    SocketClient backendClient() {
        return new SocketClientImpl(backendClientName);
    }

    @Bean
    SocketServer backendServer(SocketService backendService) {
        return new SocketServerImpl(backendClientName, backendPort, backendService);
    }
}

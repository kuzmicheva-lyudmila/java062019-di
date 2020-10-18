package ru.otus.hw.webserver.frontend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.messageserver.*;

@Configuration
public class FrontendConfig {
    @Value("${frontend.host}")
    String frontendHost;

    @Value("${frontend.port}")
    int frontendPort;

    @Value("${message-client.frontend.name}")
    String frontendClientName;

    @Value("${messagesystem.host}")
    String messageSystemHost;

    @Value("${messagesystem.port}")
    int messageSystemPort;

    @Bean
    SocketURL frontendURL() {
        return new SocketURL(frontendHost, frontendPort, frontendClientName);
    }

    @Bean
    SocketURL messageSystemURL() {
        return new SocketURL(messageSystemHost, messageSystemPort, "");
    }

    @Bean
    SocketClient frontendClient() {
        return new SocketClientImpl(frontendClientName);
    }

    @Bean
    SocketServer frontendServer(SocketService frontendService) {
        return new SocketServerImpl(frontendClientName, frontendPort, frontendService);
    }
}

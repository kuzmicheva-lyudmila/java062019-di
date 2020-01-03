package ru.otus.hw.webserver.configuration;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import ru.otus.hw.webserver.messagesystem.MessageClient;
import ru.otus.hw.webserver.messagesystem.MessageClientImpl;
import ru.otus.hw.webserver.messagesystem.MessageSystem;

@Configuration
public class AppConfiguration {

    @Bean
    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
    public MessageClient createMessageClient(String name, MessageSystem messageSystem) {
        return new MessageClientImpl(name, messageSystem);
    }
}

package ru.otus.hw.webserver.service.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.otus.hw.webserver.messagesystem.Message;
import ru.otus.hw.webserver.messagesystem.MessageType;
import ru.otus.hw.webserver.messagesystem.RequestHandler;
import ru.otus.hw.webserver.messagesystem.Serializers;
import ru.otus.hw.webserver.models.User;
import ru.otus.hw.webserver.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GetUserDataResponseHandler implements RequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetUserDataResponseHandler.class);

    private final UserService userService;

    public GetUserDataResponseHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            MessageType messageType = MessageType.getValue(msg.getType());
            switch (messageType) {
                case USER_CREATE:
                    UUID userCreateMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
                    userService.takeConsumer(messageType, userCreateMessageId, null).ifPresent(consumer -> consumer.accept(null));
                    break;
                case USER_LIST:
                    List userData = Serializers.deserialize(msg.getPayload(), List.class);
                    UUID userListMessageId = msg.getSourceMessageId().orElseThrow(() -> new RuntimeException("Not found sourceMsg for message:" + msg.getId()));
                    userService.takeConsumer(messageType, userListMessageId, List.class).ifPresent(consumer -> consumer.accept(userData));
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
        return Optional.empty();
    }
}

package ru.otus.hw.webserver.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.hw.webserver.messagesystem.*;
import ru.otus.hw.webserver.models.User;
import ru.otus.hw.webserver.frontend.handlers.GetUserDataResponseHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Service
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final Map<UUID, Consumer<?>> consumerMap = new ConcurrentHashMap<>();
    private final MessageClient messageClient;
    private final String databaseServiceClientName;

    public UserServiceImpl(
            MessageClient frontendMessageClient,
            @Value("${message-client.database.name}") String databaseServiceClientName
    ) {
        ResponseHandler responseHandler = new GetUserDataResponseHandler(this);
        this.messageClient = frontendMessageClient;
        this.messageClient.addHandler(MessageType.USER_CREATE, responseHandler);
        this.messageClient.addHandler(MessageType.USER_LIST, responseHandler);

        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void createUser(User objectData, Consumer<User> dataConsumer) {
        Message outMessage= messageClient.produceMessage(databaseServiceClientName, objectData, MessageType.USER_CREATE);
        consumerMap.put(outMessage.getId(), dataConsumer);
        messageClient.sendMessage(outMessage);
    }

    @Override
    public <T> Optional<Consumer<T>> takeConsumer(MessageType messageType, UUID sourceMessageId, Class<T> tClass) {
        Consumer<T> consumer = (Consumer<T>) consumerMap.remove(sourceMessageId);
        if (consumer == null) {
            logger.warn("consumer not found for:{}", sourceMessageId);
            return Optional.empty();
        }
        switch (messageType) {
            case USER_CREATE:
            case USER_LIST:
                return Optional.of(consumer);
            default:
                return Optional.empty();
        }
    }

    @Override
    public void loadAll(Consumer<List<User>> dataSupplier) {
        Message outMessage = messageClient.produceMessage(databaseServiceClientName, null, MessageType.USER_LIST);
        consumerMap.put(outMessage.getId(), dataSupplier);
        messageClient.sendMessage(outMessage);
    }
}

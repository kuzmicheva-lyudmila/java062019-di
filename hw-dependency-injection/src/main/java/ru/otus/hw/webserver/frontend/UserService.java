package ru.otus.hw.webserver.frontend;

import ru.otus.hw.webserver.messagesystem.MessageType;
import ru.otus.hw.webserver.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface UserService {
    void createUser(User objectData, Consumer<User> dataConsumer);

    <T> Optional<Consumer<T>> takeConsumer(MessageType messageType, UUID sourceMessageId, Class<T> tClass);

    void loadAll(Consumer<List<User>> dataSupplier);
}

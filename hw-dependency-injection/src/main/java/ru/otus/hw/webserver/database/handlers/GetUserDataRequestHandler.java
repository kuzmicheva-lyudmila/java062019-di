package ru.otus.hw.webserver.database.handlers;

import org.springframework.stereotype.Component;
import ru.otus.hw.webserver.database.dao.UserDao;
import ru.otus.hw.webserver.messagesystem.*;
import ru.otus.hw.webserver.models.User;

import java.util.List;
import java.util.Optional;

import static ru.otus.hw.webserver.messagesystem.MessageType.*;

@Component
public class GetUserDataRequestHandler implements RequestHandler {
    private final UserDao userDao;

    public GetUserDataRequestHandler(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        MessageType messageType = MessageType.getValue(msg.getType());
        switch (messageType) {
            case USER_LIST:
                List<User> data = userDao.loadAll();
                return Optional.of(
                        new Message(
                                msg.getTo(),
                                msg.getFrom(),
                                Optional.of(msg.getId()),
                                USER_LIST.getValue(),
                                Serializers.serialize(data)
                        )
                );
            case USER_CREATE:
                User user = Serializers.deserialize(msg.getPayload(), User.class);
                userDao.create(user);
                return Optional.of(
                        new Message(
                                msg.getTo(),
                                msg.getFrom(),
                                Optional.of(msg.getId()),
                                USER_CREATE.getValue(),
                                Serializers.serialize(null)
                        )
                );
            default:
                break;
        }
        return Optional.empty();
    }
}

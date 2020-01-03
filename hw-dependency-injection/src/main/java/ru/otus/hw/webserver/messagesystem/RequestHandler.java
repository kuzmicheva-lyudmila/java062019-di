package ru.otus.hw.webserver.messagesystem;

import java.util.Optional;

public interface RequestHandler {
    Optional<Message> handle(Message msg);
}

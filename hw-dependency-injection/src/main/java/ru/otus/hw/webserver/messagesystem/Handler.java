package ru.otus.hw.webserver.messagesystem;

import java.util.Optional;

public interface Handler {
    Optional<Message> handle(Message msg);
}

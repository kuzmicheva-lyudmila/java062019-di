package ru.otus.hw.webserver.backend.service;

import ru.otus.hw.messageserver.Message;
import ru.otus.hw.messageserver.SocketService;

public interface BackendService extends SocketService {
    @Override
    boolean getMessage(Message message);
}

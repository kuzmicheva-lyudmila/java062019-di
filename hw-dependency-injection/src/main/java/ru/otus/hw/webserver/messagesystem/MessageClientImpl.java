package ru.otus.hw.webserver.messagesystem;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class MessageClientImpl implements MessageClient {
    private static final Logger logger = LoggerFactory.getLogger(MessageClientImpl.class);

    private final String name;
    private final MessageSystem messageSystem;
    private final Map<String, Handler> handlers = new ConcurrentHashMap<>();

    public MessageClientImpl(String name, MessageSystem messageSystem) {
        this.name = name;
        this.messageSystem = messageSystem;
    }

    @Override
    public void addHandler(MessageType type, Handler handler) {
        this.handlers.put(type.getValue(), handler);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean sendMessage(Message msg) {
        boolean result = messageSystem.newMessage(msg);
        if (!result) {
            logger.error("the last message was rejected: {}", msg);
        }
        return result;
    }

    @Override
    public void handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            Handler handler = handlers.get(msg.getType());
            if (handler != null) {
                handler.handle(msg).ifPresent(this::sendMessage);
            } else {
                logger.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            logger.error("msg:" + msg, ex);
        }
    }

    @Override
    public <T> Message produceMessage(String to, T data, MessageType msgType) {
        return new Message(name, to, null, msgType.getValue(), Serializers.serialize(data));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageClientImpl msClient = (MessageClientImpl) o;
        return Objects.equals(name, msClient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}


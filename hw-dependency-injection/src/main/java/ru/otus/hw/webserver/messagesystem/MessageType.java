package ru.otus.hw.webserver.messagesystem;

import java.util.Arrays;

public enum MessageType {
    USER_LIST("UserList"),
    USER_CREATE("UserCreate");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public static MessageType getValue(String typeString) {
        return Arrays.stream(MessageType.values())
                .filter(e -> e.value.equals(typeString))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported messageType %s.", typeString)));
    }

    public String getValue() {
        return value;
    }
}
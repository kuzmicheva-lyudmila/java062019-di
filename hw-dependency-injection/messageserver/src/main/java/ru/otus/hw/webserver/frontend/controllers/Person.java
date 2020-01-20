package ru.otus.hw.webserver.frontend.controllers;

import lombok.Data;

@Data
public class Person {
    private final long id;
    private final String fullname;
    private final int age;
}

package ru.otus.hw.webserver.frontend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import ru.otus.hw.webserver.frontend.service.FrontendService;

import java.io.IOException;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final FrontendService frontendService;

    public UserController(FrontendService frontendService) {

        this.frontendService = frontendService;
    }

    @MessageMapping("/list-users")
    public void userListView() throws IOException {
        frontendService.loadAll();
    }

    @MessageMapping("/create-user")
    public void userSave(Person person) throws IOException {
        frontendService.createUser(person);
    }
}

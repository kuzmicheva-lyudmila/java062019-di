package ru.otus.hw.webserver.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.hw.webserver.models.User;
import ru.otus.hw.webserver.frontend.UserService;

@Controller
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final SimpMessagingTemplate template;

    public UserController(UserService userService,
                          SimpMessagingTemplate template) {
        this.userService = userService;
        this.template = template;
    }

    @MessageMapping("/list-users")
    public void userListView() {
        userService.loadAll(
            data -> template.convertAndSend("/topic/users", data)
        );
    }

    @MessageMapping("/create-user")
    public void userSave(User user) {
        userService.createUser(user, data -> logger.info("the user created success: {}", user.toString()));
    }
}

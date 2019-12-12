package ru.otus.hw.webserver.controllers;

import com.sun.istack.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.hw.webserver.models.AddressDataSet;
import ru.otus.hw.webserver.models.PhoneDataSet;
import ru.otus.hw.webserver.models.User;
import ru.otus.hw.webserver.service.dbservice.UserService;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private static final String PARAMETER_USER_NAME = "userName";
    private static final String PARAMETER_USER_AGE = "userAge";
    private static final String PARAMETER_USER_ADDRESS = "userAddress";
    private static final String PARAMETER_USER_PHONE = "userPhone";

    private static final String PAGE_USER_LIST = "userList.html";
    private static final String PAGE_USER_CREATE = "userCreate.html";

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/user/list"})
    public String userListView(@NotNull Model model) {
        List<String> users = userService.loadAll().stream()
                .map(User::toString)
                .collect(Collectors.toList());

        model.addAttribute("users", users);
        return PAGE_USER_LIST;
    }

    @GetMapping("/user/create")
    public String userCreateView(@NotNull Model model) {
        model.addAttribute("userName", "");
        model.addAttribute("userAge", "");
        return PAGE_USER_CREATE;
    }

    @PostMapping("/user/save")
    public RedirectView userSave(
            @ModelAttribute("userName") String userName,
            @ModelAttribute("userAge") String userAge
    ) {
        User user = new User();
        user.setName(userName);
        user.setAge(Integer.getInteger(userAge, 10));

        userService.create(user);
        return new RedirectView("/user/list", true);
    }

}

package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.configs.SuccessUserHandler;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UsersController {
    private final UserService userService;
    private final SuccessUserHandler successUserHandler;

    @Autowired
    public UsersController(UserService userService, SuccessUserHandler successUserHandler) {
        this.userService = userService;
        this.successUserHandler = successUserHandler;
    }

    @GetMapping()
    public String showUserInfo(Model model) {
        long id = successUserHandler.getCurrentUser().getId();
        User user = userService.getUser(id);
        model.addAttribute("user", user);

        return "/forUser/user";
    }
}

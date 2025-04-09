package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // Внедрение зависимости через конструктор
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getUser(@RequestParam(value = "id", required = false) Integer id) {
        ModelAndView modelAndView = new ModelAndView("user");

        // Если ID указан - получаем пользователя по ID
        if (id != null) {
            modelAndView.addObject("user", userService.getUserById(id));
        }
        // Если ID не указан - получаем текущего аутентифицированного пользователя
        else {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();
            modelAndView.addObject("user", userService.loadUserByUsername(currentUser.getUsername()));
        }

        return modelAndView;
    }
}

package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin") // Общий префикс для всех эндпоинтов (п.9)
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    // PasswordEncoder удален из контроллера (п.7)
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public ModelAndView showAllUsers(@ModelAttribute("newUser") User newUser) {
        ModelAndView modelAndView = new ModelAndView("admin");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();

        modelAndView.addObject("roles", user.getRoles());
        modelAndView.addObject("user", userService.loadUserByUsername(user.getUsername()));
        modelAndView.addObject("userList", userService.getAllUsers());
        modelAndView.addObject("allRoles", roleService.getAllRoles());

        return modelAndView; // Возвращаем ModelAndView (п.8)
    }

    @DeleteMapping
    public ModelAndView deleteUser(@RequestParam("id") int id) { // RequestParam вместо PathVariable (п.10)
        userService.removeUserById(id);
        return new ModelAndView("redirect:/admin");
    }

    @PostMapping
    public ModelAndView createUser(@ModelAttribute("newUser") User user) {
        userService.addUser(user); // Кодирование пароля внутри сервиса (п.7)
        return new ModelAndView("redirect:/admin");
    }

    @PatchMapping
    public ModelAndView updateUser(
            @ModelAttribute("userEdit") User userEdit,
            @RequestParam("id") int id) { // RequestParam вместо PathVariable (п.10)

        userService.updateUser(id, userEdit); // Вся логика внутри сервиса (п.7)
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping("/findUser")
    @ResponseBody
    public User findUser(@RequestParam("id") Integer id) { // RequestParam вместо PathVariable (п.10)
        return userService.getUserById(id);
    }
}
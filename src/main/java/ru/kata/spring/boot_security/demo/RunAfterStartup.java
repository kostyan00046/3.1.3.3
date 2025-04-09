package ru.kata.spring.boot_security.demo;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.Set;

@Component
public class RunAfterStartup {
    private final UserServiceImpl userService;
    private final RoleServiceImpl roleService;


    public RunAfterStartup(UserServiceImpl userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    Role adminRole = new Role("ROLE_ADMIN");
    Role userRole = new Role("ROLE_USER");
    Set<Role> adminAndUserRoles = Set.of(adminRole, userRole);
    Set<Role> userRoles = Set.of(userRole);
    User admin = new User("admin", "admin", 20,
            "admin", "admin@mail.ru", adminAndUserRoles);
    User user = new User("user", "user", 35,
            "user", "user@mail.ru", userRoles);



    public void addUser(User user) {
        userService.addUser(user);
    }

    public void addRole(Role role) {
        roleService.addRole(role);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runAfterStartup() {
        addRole(adminRole);
        addRole(userRole);
        addUser(admin);
        addUser(user);
    }
}
package ru.kata.spring.boot_security.demo.Dao;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;


public interface RoleDao {
    void addRole(Role role);
    List<Role> getAllRoles();
}

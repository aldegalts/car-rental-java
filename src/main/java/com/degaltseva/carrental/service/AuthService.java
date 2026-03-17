package com.degaltseva.carrental.service;

import com.degaltseva.carrental.model.Role;
import com.degaltseva.carrental.model.User;
import com.degaltseva.carrental.repository.RoleRepository;
import com.degaltseva.carrental.repository.UserRepository;
import com.degaltseva.carrental.util.PasswordUtil;

import java.util.Optional;

public class AuthService {

    private final UserRepository userRepository = new UserRepository();
    private final RoleRepository roleRepository = new RoleRepository();

    public User register(String username, String password) {
        Optional<User> existing = userRepository.findByUsername(username);
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким именем уже существует");
        }

        Role userRole = roleRepository.findByRoleName("user")
                .orElseThrow(() -> new RuntimeException("Роль 'user' не найдена"));

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(PasswordUtil.hash(password));
        user.setRoleId(userRole.getId());
        user = userRepository.save(user);

        user.setRoleName(userRole.getRoleName());
        return user;
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Неверное имя пользователя или пароль"));

        if (!PasswordUtil.verify(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Неверное имя пользователя или пароль");
        }

        Role role = roleRepository.findById(user.getRoleId())
                .orElseThrow(() -> new RuntimeException("Роль не найдена"));
        user.setRoleName(role.getRoleName());

        return user;
    }
}

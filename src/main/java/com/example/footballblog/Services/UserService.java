package com.example.footballblog.Services;

import com.example.footballblog.Models.Role;
import com.example.footballblog.Models.User;
import com.example.footballblog.Repo.RoleRepository;
import com.example.footballblog.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    @Autowired
    private UserValidationService userValidationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String saveUser(User user) {
//        if (userValidationService.isUserLoginExists(user)) {
//            return "User with this login already exists";
//        }

        if (userValidationService.isUserEmailExists(user)) {
            return "User with this email already exists";
        }

        String validationError = userValidationService.validateUserData(user);
        if (validationError != null) {
            return validationError;
        }

        // Шифруем пароль
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Сохраняем пользователя в базе данных
        userRepository.save(user);
        return ""; // Возвращаем пустую строку при успешном сохранении
    }

    public List<Role> getAllRoles() {

        return roleRepository.findAll();
    }

    public void assignRoleToUser(User user, Role role) {
        user.setRole(role);
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}

package com.example.footballblog.Services;

import com.example.footballblog.Models.Role;
import com.example.footballblog.Models.Status;
import com.example.footballblog.Models.User;
import com.example.footballblog.Repo.RoleRepository;
import com.example.footballblog.Repo.StatusRepository;
import com.example.footballblog.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
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
        return "";
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
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public List<Status> getAllStatuses() {
        return statusRepository.findAll();
    }

    public User updateUser(Long id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword())); // Password encryption
        existingUser.setRole(updatedUser.getRole());
        existingUser.setStatus(updatedUser.getStatus());
        return userRepository.save(existingUser);  // Save updated user
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);  // Delete user by ID
    }
}

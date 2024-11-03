package com.example.footballblog.Services;

import com.example.footballblog.Models.User;
import com.example.footballblog.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class UserValidationService {

    @Autowired
    private UserRepository userRepository;

    public boolean isUserExists(Long id) {
        return userRepository.existsById(id);
    }

//    public boolean isUserLoginExists(User user) {
//        if (user.getId() == null) {
//            return userRepository.existsByUsername(user.getUsername());
//        }
//        return userRepository.existsByUsernameAndIdNot(user.getUsername(), user.getId());
//    }


    public boolean isUserEmailExists(User user) {
        if (user.getId() == null) {
            return userRepository.existsByEmail(user.getEmail());
        }
        return userRepository.existsByEmailAndIdNot(user.getEmail(), user.getId());
    }



    public String validateUserData(User user) {

        if (user.getUsername().length() < 3) {
            return "Login must contain at least 3 characters.";
        }

        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{5,}$";
        if (!Pattern.matches(passwordRegex, user.getPassword())) {
            return "The password must contain at least 6 characters.";
        }

        return null;
    }
}

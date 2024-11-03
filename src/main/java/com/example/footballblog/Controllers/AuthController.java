package com.example.footballblog.Controllers;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверное имя пользователя или пароль.");
        }
        return "login"; // Возвращаем имя шаблона для страницы логина
    }

    @GetMapping("/dashboard") // Страница, доступная после входа
    public String dashboard(Model model, Authentication authentication) {
        // Проверяем, что пользователь аутентифицирован
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            model.addAttribute("username", username);
            // Здесь можно добавить любую другую необходимую логику, связанную с блогом
            return "dashboard"; // Возвращаем имя шаблона для страницы после входа
        }
        // Если пользователь не аутентифицирован, перенаправляем на страницу логина
        return "redirect:/login";
    }
}
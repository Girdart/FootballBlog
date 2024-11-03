package com.example.footballblog.Controllers;


import com.example.footballblog.Models.User;
import com.example.footballblog.Services.RoleService;
import com.example.footballblog.Services.StatusService;
import com.example.footballblog.Services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;




@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private StatusService statusService;

    @GetMapping("/createUser")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User()); // Добавляем объект User для формы
        return "createUser"; // Имя вашего шаблона
    }

    @PostMapping("/createUser")
    public ModelAndView createUser(@ModelAttribute User user, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        // Присваиваем статус и роль по умолчанию
        if (user.getStatus() == null) {
            user.setStatus(statusService.getDefaultStatus()); // Получаем статус по умолчанию
        }
        if (user.getRole() == null) {
            user.setRole(roleService.getDefaultRole()); // Получаем роль по умолчанию
        }

        // Сохранение пользователя
        String errorMessage = userService.saveUser(user);
        if (!errorMessage.isEmpty()) {
            modelAndView.setViewName("createUser");
            modelAndView.addObject("user", user); // Передаем обратно объект пользователя
            modelAndView.addObject("error", errorMessage);
            return modelAndView; // Возвращаем форму с сообщением об ошибке
        }

        modelAndView.setViewName("redirect:/");

        // Сохранение пользователя в сессии
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        return modelAndView;
    }
//    @GetMapping("/login")
//    public String loginPage() {
//        return "login"; // Отображаем шаблон login.html
//    }
}
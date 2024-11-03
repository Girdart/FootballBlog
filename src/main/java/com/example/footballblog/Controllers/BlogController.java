package com.example.footballblog.Controllers;
import com.example.footballblog.Models.User;
import com.example.footballblog.Repo.UserRepository;
import org.springframework.ui.Model;

import com.example.footballblog.Models.Blog;
import com.example.footballblog.Repo.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    UserRepository userService;
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    // Просмотр всех блогов
    @GetMapping
    public String viewAllBlogs(Model model) {
        model.addAttribute("blogs", blogRepository.findAll());
        return "blog";
    }

    @GetMapping("/add")
    public String showAddBlogForm(Model model, Principal principal) {


        Optional<User> user = userService.findByUsername(principal.getName());
        model.addAttribute("userId", user.get().getId());
        model.addAttribute("blog", new Blog());
        return "addBlog";
    }

    // Создание нового блога
    @PostMapping("/add")
    public String addBlog(@ModelAttribute Blog blog, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found")); // Извлечение пользователя или ошибка
        blog.setUser(user);
        blog.setCreatedAt(LocalDateTime.now());
        blogRepository.save(blog);
        return "redirect:/blog";
    }

    // Форма для редактирования блога
    @GetMapping("/edit/{id}")
    public String showEditBlogForm(@PathVariable Long id, Model model) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found")); // Обработка ошибки, если блог не найден
        model.addAttribute("blog", blog);
        return "editBlog";
    }

    // Редактирование блога
    @PostMapping("/edit/{id}")
    public String updateBlog(@PathVariable Long id, @ModelAttribute Blog blog) {
        blog.setId(id);
        blogRepository.save(blog);
        return "redirect:/blog";
    }

    // Удаление блога
    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogRepository.deleteById(id);
        return "redirect:/blog";
    }
}

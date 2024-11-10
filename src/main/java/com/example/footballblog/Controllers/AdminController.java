package com.example.footballblog.Controllers;

import org.springframework.ui.Model;
import com.example.footballblog.Models.Blog;
import com.example.footballblog.Models.Post;
import com.example.footballblog.Models.User;
import com.example.footballblog.Services.BlogService;
import com.example.footballblog.Services.PostService;
import com.example.footballblog.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private PostService postService;

    @GetMapping
    public String adminDashboard(Model model, Authentication authentication) {
        // Проверяем, что пользователь — администратор
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            if ("Admin".equals(user.getRole().getName())) {
                model.addAttribute("users", userService.getAllUsers());
                model.addAttribute("blogs", blogService.getAllBlogs());
                model.addAttribute("posts", postService.getAllPosts());
                return "adminDashboard";
            } else {
                return "redirect:/dashboard"; // Если пользователь не администратор, перенаправляем на дашборд
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/editUser/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("statuses", userService.getAllStatuses());
        return "editUser";
    }

    @PostMapping("/editUser/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    // Методы для работы с блогами и постами аналогичны
    @GetMapping("/editBlog/{id}")
    public String editBlog(@PathVariable Long id, Model model) {
        Blog blog = blogService.getBlogById(id);
        model.addAttribute("blog", blog);
        return "editBlog";
    }

    @PostMapping("/editBlog/{id}")
    public String updateBlog(@PathVariable Long id, @ModelAttribute Blog blog) {
        blogService.updateBlog(id, blog);
        return "redirect:/admin";
    }

    @GetMapping("/deleteBlog/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return "redirect:/admin";
    }

    @GetMapping("/editPost/{id}")
    public String editPost(@PathVariable Long id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "editPost";
    }

    @PostMapping("/editPost/{id}")
    public String updatePost(@PathVariable Long id, @ModelAttribute Post post) {
        postService.updatePost(id, post);
        return "redirect:/admin";
    }

    @GetMapping("/deletePost/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "redirect:/admin";
    }
}

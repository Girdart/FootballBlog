package com.example.footballblog.Controllers;

import com.example.footballblog.Models.Blog;
import com.example.footballblog.Models.Post;
import org.springframework.ui.Model;
import com.example.footballblog.Repo.BlogRepository;
import com.example.footballblog.Repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/blog/{blogId}/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BlogRepository blogRepository;

    // Просмотр всех постов в блоге
    @GetMapping
    public String viewAllPosts(@PathVariable Long blogId, Model model) {
        model.addAttribute("posts", postRepository.findByBlogId(blogId));
        model.addAttribute("blogId", blogId);
        return "viewPosts";
    }

    // Форма для создания поста
    @GetMapping("/add")
    public String showAddPostForm(@PathVariable Long blogId, Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("blogId", blogId);
        return "addPost";
    }

    // Создание нового поста
    @PostMapping("/add")
    public String addPost(@PathVariable Long blogId, @ModelAttribute Post post) {
        Optional<Blog> blog = blogRepository.findById(blogId);
        blog.ifPresent(post::setBlog);
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
        return "redirect:/blog/" + blogId + "/post";
    }

    // Форма для редактирования поста
    @GetMapping("/edit/{postId}")
    public String showEditPostForm(@PathVariable Long blogId, @PathVariable Long postId, Model model) {
        Optional<Post> post = postRepository.findById(postId);
        post.ifPresent(value -> model.addAttribute("post", value));
        model.addAttribute("blogId", blogId);
        return "editPost";
    }

    // Редактирование поста
    @PostMapping("/edit/{postId}")
    public String updatePost(@PathVariable Long blogId, @PathVariable Long postId, @ModelAttribute Post post) {
        post.setId(postId);
        post.setBlog(blogRepository.findById(blogId).orElse(null));
        postRepository.save(post);
        return "redirect:/blog/" + blogId + "/post";
    }

    // Удаление поста
    @GetMapping("/delete/{postId}")
    public String deletePost(@PathVariable Long blogId, @PathVariable Long postId) {
        postRepository.deleteById(postId);
        return "redirect:/blog/" + blogId + "/post";
    }
}

package com.example.footballblog.Controllers;
import com.example.footballblog.Models.Post;
import com.example.footballblog.Models.User;
import com.example.footballblog.Repo.PostRepository;
import com.example.footballblog.Repo.UserRepository;
import com.example.footballblog.Services.BlogService;
import org.springframework.ui.Model;

import com.example.footballblog.Models.Blog;
import com.example.footballblog.Repo.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;



@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogService blogService;

    // Просмотр всех блогов
    @GetMapping
    public String viewAllBlogs(Model model) {
        model.addAttribute("blogs", blogRepository.findAll());
        return "blog";
    }

    // Просмотр дополнительной информации о блоге
    @GetMapping("/view/{id}")
    public String viewBlogDetails(@PathVariable Long id, Model model) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        model.addAttribute("blog", blog);
        model.addAttribute("posts", postRepository.findByBlogId(id));
        return "blogDetails";
    }

    // Переход на страницу добавления поста
    @GetMapping("/view/{id}/addPost")
    public String showAddPostForm(@PathVariable Long id, Model model) {
        model.addAttribute("post", new Post());
        model.addAttribute("blogId", id);
        return "addPost";
    }

    @PostMapping("/view/{id}/addPost")
    public String addPostToBlog(@PathVariable Long id, @ModelAttribute Post post) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        post.setBlog(blog);
        post.setCreatedAt(LocalDateTime.now());
        postRepository.save(post);
        return "redirect:/blog/view/" + id;
    }
    //  методы для редактирования и удаления постов
    @GetMapping("/post/edit/{postId}")
    public String showEditPostForm(@PathVariable Long postId, Model model, Principal principal) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Проверка, что текущий пользователь является создателем поста
        if (!post.getBlog().getUser().getUsername().equals(principal.getName())) {
            return "redirect:/blog/view/" + post.getBlog().getId();
        }

        model.addAttribute("post", post);
        return "editPost";
    }

    @PostMapping("/post/edit/{postId}")
    public String updatePost(@PathVariable Long postId, @ModelAttribute Post post, Principal principal) {
        Post existingPost = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Проверка, что текущий пользователь является создателем поста
        if (!existingPost.getBlog().getUser().getUsername().equals(principal.getName())) {
            return "redirect:/blog/view/" + existingPost.getBlog().getId();
        }

        existingPost.setTitle(post.getTitle());
        existingPost.setDescription(post.getDescription());
        postRepository.save(existingPost);

        return "redirect:/blog/view/" + existingPost.getBlog().getId();
    }

    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable Long postId, Principal principal) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Проверка, что текущий пользователь является создателем поста
        if (!post.getBlog().getUser().getUsername().equals(principal.getName())) {
            return "redirect:/blog/view/" + post.getBlog().getId();
        }

        postRepository.delete(post);
        return "redirect:/blog/view/" + post.getBlog().getId();
    }

    // Добавление нового блога
    @GetMapping("/add")
    public String showAddBlogForm(Model model, Principal principal) {
        Optional<User> user = userRepository.findByUsername(principal.getName());
        model.addAttribute("userId", user.get().getId());
        model.addAttribute("blog", new Blog());
        return "addBlog";
    }

    @PostMapping("/add")
    public String addBlog(@ModelAttribute Blog blog, @RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        blog.setUser(user);
        blog.setCreatedAt(LocalDateTime.now());
        blogRepository.save(blog);
        return "redirect:/blog";
    }

    // Форма для редактирования блога
    @GetMapping("/edit/{id}")
    public String showEditBlogForm(@PathVariable Long id, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        // Получаем имя текущего пользователя
        String currentUsername = principal.getName();
        // Проверяем, совпадает ли имя текущего пользователя с именем создателя блога
        if (!blog.getUser().getUsername().equals(currentUsername)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Вы не можете редактировать этот блог, так как вы не являетесь его создателем.");
            return "redirect:/blog"; // Перенаправляем на главную страницу блога
        }

        model.addAttribute("blog", blog);
        return "editBlog";
    }



    // Редактирование блога
    @PostMapping("/edit/{id}")
    public String updateBlog(@PathVariable Long id, @ModelAttribute Blog blog) {
        Blog existingBlog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        existingBlog.setTitle(blog.getTitle());
        existingBlog.setContent(blog.getContent());
        blogRepository.save(existingBlog);
        return "redirect:/blog"; // Перенаправляем на страницу со списком блогов
    }

    

    // Удаление блога и всех его постов
    @PostMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));

        String currentUsername = principal.getName();
        if (!blog.getUser().getUsername().equals(currentUsername)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Вы не можете удалить этот блог, так как вы не являетесь его создателем.");
            return "redirect:/blog";
        }

        blogService.deleteBlog(id);
        return "redirect:/blog";
    }
}

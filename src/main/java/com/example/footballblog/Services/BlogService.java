package com.example.footballblog.Services;

import com.example.footballblog.Models.Blog;
import com.example.footballblog.Repo.BlogRepository;
import com.example.footballblog.Repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlogService {

    private final BlogRepository blogRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }

    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();  // Fetch all blogs
    }

    public Blog getBlogById(Long id) {
        return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Blog not found"));
    }

    @Transactional
    public Blog updateBlog(Long id, Blog updatedBlog) {
        Blog existingBlog = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Blog not found"));
        // Update fields as needed
        existingBlog.setTitle(updatedBlog.getTitle());
        existingBlog.setContent(updatedBlog.getContent());
        return blogRepository.save(existingBlog);  // Save updated blog
    }

    @Transactional
    public void deleteBlog(Long blogId) {
        // Delete all posts related to the blog
        postRepository.deleteByBlogId(blogId);
        // Delete the blog itself
        blogRepository.deleteById(blogId);
    }
}
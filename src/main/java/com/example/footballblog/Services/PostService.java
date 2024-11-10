package com.example.footballblog.Services;

import com.example.footballblog.Models.Post;
import com.example.footballblog.Repo.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();  // Fetch all posts
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
    }

    public Post updatePost(Long id, Post updatedPost) {
        Post existingPost = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post not found"));
        // Update fields as needed
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setDescription(updatedPost.getDescription());
        existingPost.setBlog(updatedPost.getBlog());  // Update associated blog
        return postRepository.save(existingPost);  // Save updated post
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);  // Delete post by ID
    }
}

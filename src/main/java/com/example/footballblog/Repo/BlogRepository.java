package com.example.footballblog.Repo;

import com.example.footballblog.Models.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Long> {}

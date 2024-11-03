package com.example.footballblog.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private Float rating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Blog(String title, String content, Float rating) {
        this.title = title;
        this.content = content;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
    }
}

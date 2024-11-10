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
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO )
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    private Blog blog;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column
    private Float rating;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Post(Blog blog, String title, String description, Float rating) {
        this.blog = blog;
        this.title = title;
        setDescription(description);
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
    }


}

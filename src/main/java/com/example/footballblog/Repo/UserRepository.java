package com.example.footballblog.Repo;

import com.example.footballblog.Models.Status;
import com.example.footballblog.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
    List<User> findByStatus(Status status);
    Optional<User> findByUsername(String username);


}

package com.example.footballblog.Repo;

import com.example.footballblog.Models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusRepository extends JpaRepository<Status, Long> {

}

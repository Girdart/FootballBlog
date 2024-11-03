package com.example.footballblog.Services;

import com.example.footballblog.Models.Status;
import com.example.footballblog.Repo.StatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    @Autowired
    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Status getDefaultStatus() {
        // Здесь можно указать, например, что статус по умолчанию – это статус с определённым ID или названием
        return statusRepository.findById(1L).orElseThrow(() ->
                new IllegalStateException("Default status not found"));
    }
}

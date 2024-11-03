package com.example.footballblog.Services;

import com.example.footballblog.Models.Role;
import com.example.footballblog.Repo.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getDefaultRole() {
        // Здесь можно указать, что роль по умолчанию – это роль с определённым ID или названием
        return roleRepository.findById(2L).orElseThrow(() ->
                new IllegalStateException("Default role not found"));
    }
}
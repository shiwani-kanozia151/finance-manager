package com.finance.service;

import com.finance.entity.Role;
import com.finance.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name)
            .orElseThrow(() -> new RuntimeException("Role not found: " + name));
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public void initializeDefaultRoles() {
        if (roleRepository.findByName("VIEWER").isEmpty()) {
            roleRepository.save(new Role("VIEWER", "Can only view dashboard data"));
        }
        if (roleRepository.findByName("ANALYST").isEmpty()) {
            roleRepository.save(new Role("ANALYST", "Can view records and access insights"));
        }
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            roleRepository.save(new Role("ADMIN", "Can create, update, and manage records and users"));
        }
    }
}

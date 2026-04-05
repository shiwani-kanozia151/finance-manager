package com.finance.service;

import com.finance.dto.UserResponse;
import com.finance.entity.Role;
import com.finance.entity.User;
import com.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse registerUser(String name, String email, String password, String roleName) {
        // Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // Get role
        Role role = roleService.getRoleByName(roleName);

        // Create user with encoded password
        User user = new User(name, email, passwordEncoder.encode(password), role);
        user = userRepository.save(user);

        return new UserResponse(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<UserResponse> getAllActiveUsers() {
        return userRepository.findByActive(true)
            .stream()
            .map(UserResponse::new)
            .collect(Collectors.toList());
    }

    public UserResponse updateUser(Long id, String name, String email) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(name);
        if (!email.equals(user.getEmail())) {
            if (userRepository.findByEmail(email).isPresent()) {
                throw new RuntimeException("Email already in use");
            }
            user.setEmail(email);
        }

        user = userRepository.save(user);
        return new UserResponse(user);
    }

    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    public void activateUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}

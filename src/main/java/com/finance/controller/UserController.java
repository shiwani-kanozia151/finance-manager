package com.finance.controller;

import com.finance.dto.AuthResponse;
import com.finance.dto.LoginRequest;
import com.finance.dto.RegisterRequest;
import com.finance.dto.UserResponse;
import com.finance.entity.User;
import com.finance.security.JwtProvider;
import com.finance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UserResponse userResponse = userService.registerUser(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getRole()
            );

            String token = jwtProvider.generateToken(userResponse.getEmail(), userResponse.getRole());

            AuthResponse authResponse = new AuthResponse(
                token,
                userResponse.getEmail(),
                userResponse.getRole(),
                userResponse.getId()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            User user = userService.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

            if (!user.getActive()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User account is inactive");
            }

            if (!userService.validatePassword(request.getPassword(), user.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }

            String token = jwtProvider.generateToken(user.getEmail(), user.getRole().getName());

            AuthResponse authResponse = new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().getName(),
                user.getId()
            );

            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(new UserResponse(user));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserResponse request, Authentication authentication) {
        try {
            // Check if user is updating their own profile or is admin
            String email = authentication.getName();
            User currentUser = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            if (!currentUser.getId().equals(id) && !currentUser.getRole().getName().equals("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized");
            }

            UserResponse userResponse = userService.updateUser(id, request.getName(), request.getEmail());
            return ResponseEntity.ok(userResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id, Authentication authentication) {
        try {
            String email = authentication.getName();
            User currentUser = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            if (!currentUser.getRole().getName().equals("ADMIN")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only admins can deactivate users");
            }

            userService.deactivateUser(id);
            return ResponseEntity.ok("User deactivated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}

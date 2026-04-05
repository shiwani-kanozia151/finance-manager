package com.finance.controller;

import com.finance.dto.DashboardSummaryResponse;
import com.finance.entity.User;
import com.finance.service.DashboardService;
import com.finance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private UserService userService;

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            DashboardSummaryResponse summary = dashboardService.getSummary(user);
            return ResponseEntity.ok(summary);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/summary/monthly")
    public ResponseEntity<?> getMonthlySummary(
            @RequestParam int year,
            @RequestParam int month,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            if (month < 1 || month > 12) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid month (1-12)");
            }

            DashboardSummaryResponse summary = dashboardService.getMonthlySummary(user, year, month);
            return ResponseEntity.ok(summary);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}

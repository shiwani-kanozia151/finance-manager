package com.finance.controller;

import com.finance.dto.FinancialRecordRequest;
import com.finance.dto.FinancialRecordResponse;
import com.finance.entity.User;
import com.finance.service.FinancialRecordService;
import com.finance.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/records")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FinancialRecordController {

    @Autowired
    private FinancialRecordService recordService;

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createRecord(@Valid @RequestBody FinancialRecordRequest request, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            // Check if user has permission to create records
            String role = user.getRole().getName();
            if (!role.equals("ADMIN") && !role.equals("ANALYST")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to create records");
            }

            FinancialRecordResponse record = recordService.createRecord(user, request);
            return ResponseEntity.status(HttpStatus.CREATED).body(record);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllRecords(Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            List<FinancialRecordResponse> records = recordService.getRecordsByUser(user);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRecordById(@PathVariable Long id, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            FinancialRecordResponse record = recordService.getRecordById(id, user);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateRecord(@PathVariable Long id, @Valid @RequestBody FinancialRecordRequest request, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            String role = user.getRole().getName();
            if (!role.equals("ADMIN") && !role.equals("ANALYST")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to update records");
            }

            FinancialRecordResponse record = recordService.updateRecord(id, user, request);
            return ResponseEntity.ok(record);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecord(@PathVariable Long id, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            String role = user.getRole().getName();
            if (!role.equals("ADMIN") && !role.equals("ANALYST")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You don't have permission to delete records");
            }

            recordService.deleteRecord(id, user);
            return ResponseEntity.ok("Record deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/filter/type")
    public ResponseEntity<?> getRecordsByType(@RequestParam String type, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            List<FinancialRecordResponse> records = recordService.getRecordsByType(user, type);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/filter/category")
    public ResponseEntity<?> getRecordsByCategory(@RequestParam String category, Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            List<FinancialRecordResponse> records = recordService.getRecordsByCategory(user, category);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/filter/date-range")
    public ResponseEntity<?> getRecordsByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            List<FinancialRecordResponse> records = recordService.getRecordsByDateRange(user, startDate, endDate);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/recent")
    public ResponseEntity<?> getRecentRecords(
            @RequestParam(defaultValue = "10") int limit,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

            List<FinancialRecordResponse> records = recordService.getRecentRecords(user, limit);
            return ResponseEntity.ok(records);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}

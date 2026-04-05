package com.finance.config;

import com.finance.entity.FinancialRecord;
import com.finance.entity.Role;
import com.finance.entity.User;
import com.finance.repository.FinancialRecordRepository;
import com.finance.repository.RoleRepository;
import com.finance.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FinancialRecordRepository recordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeSampleUsers();
        initializeSampleRecords();
    }

    private void initializeRoles() {
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

    private void initializeSampleUsers() {
        if (userRepository.findByEmail("admin@finance.com").isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
            User admin = new User(
                "Admin User",
                "admin@finance.com",
                passwordEncoder.encode("admin123"),
                adminRole
            );
            userRepository.save(admin);
        }

        if (userRepository.findByEmail("analyst@finance.com").isEmpty()) {
            Role analystRole = roleRepository.findByName("ANALYST").orElseThrow();
            User analyst = new User(
                "Analyst User",
                "analyst@finance.com",
                passwordEncoder.encode("analyst123"),
                analystRole
            );
            userRepository.save(analyst);
        }

        if (userRepository.findByEmail("viewer@finance.com").isEmpty()) {
            Role viewerRole = roleRepository.findByName("VIEWER").orElseThrow();
            User viewer = new User(
                "Viewer User",
                "viewer@finance.com",
                passwordEncoder.encode("viewer123"),
                viewerRole
            );
            userRepository.save(viewer);
        }
    }

    private void initializeSampleRecords() {
        User admin = userRepository.findByEmail("admin@finance.com").orElse(null);
        if (admin != null && recordRepository.count() == 0) {
            // Sample income records
            recordRepository.save(new FinancialRecord(
                admin,
                new BigDecimal("5000.00"),
                FinancialRecord.RecordType.INCOME,
                "Salary",
                LocalDate.now().minusDays(30),
                "Monthly salary"
            ));

            recordRepository.save(new FinancialRecord(
                admin,
                new BigDecimal("500.00"),
                FinancialRecord.RecordType.INCOME,
                "Freelance",
                LocalDate.now().minusDays(20),
                "Freelance project"
            ));

            // Sample expense records
            recordRepository.save(new FinancialRecord(
                admin,
                new BigDecimal("1200.00"),
                FinancialRecord.RecordType.EXPENSE,
                "Rent",
                LocalDate.now().minusDays(25),
                "Monthly rent payment"
            ));

            recordRepository.save(new FinancialRecord(
                admin,
                new BigDecimal("150.00"),
                FinancialRecord.RecordType.EXPENSE,
                "Groceries",
                LocalDate.now().minusDays(15),
                "Weekly groceries"
            ));

            recordRepository.save(new FinancialRecord(
                admin,
                new BigDecimal("80.00"),
                FinancialRecord.RecordType.EXPENSE,
                "Utilities",
                LocalDate.now().minusDays(10),
                "Electricity and water"
            ));
        }
    }
}

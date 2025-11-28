package com.smartshop.util;

import com.smartshop.entity.User;
import com.smartshop.entity.enums.UserRole;
import com.smartshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    
    private final UserRepository userRepository;
    
    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User admin = new User();
            admin.setUsername("admin0");
            admin.setPassword(PasswordUtil.hashPassword("admin123"));
            admin.setRole(UserRole.ADMIN);
            userRepository.save(admin);
        }
    }
}

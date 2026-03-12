package com.book.library.service.impl;

import com.book.library.domain.UserRole;
import com.book.library.entity.User;
import com.book.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitialization implements CommandLineRunner {
    private  final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    @Override
    public  void run(String... args) {
        initializeAdminUser();
    }

    private void initializeAdminUser(){
        String adminEmail="greysonwithadmin@gmail.com";
        String adminPassword="Alinogy7";

        if (userRepository.findByEmail(adminEmail)==null){
            User user = User.builder().password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .fullName("greyson shawa")
                    .role(UserRole.ROLE_ADMIN)
                    .build();
            User admin = userRepository.save(user);
        }
    }
}

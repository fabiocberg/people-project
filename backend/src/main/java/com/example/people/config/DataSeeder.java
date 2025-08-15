package com.example.people.config;

import com.example.people.auth.UserAccount;
import com.example.people.auth.UserAccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {
    @Bean
    CommandLineRunner seedAdmin(UserAccountRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByUsername("admin")) {
                UserAccount u = new UserAccount();
                u.setUsername("admin");
                u.setPassword(encoder.encode("admin"));
                u.setEnabled(true);
                repo.save(u);
            }
        };
    }
}

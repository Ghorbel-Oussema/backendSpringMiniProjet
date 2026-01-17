package com.example.MiniProjetBackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.MiniProjetBackend.entity.Role;
import com.example.MiniProjetBackend.entity.User;
import com.example.MiniProjetBackend.repository.RoleRepository;
import com.example.MiniProjetBackend.repository.UserRepository;

@Configuration
public class DataInitConfig {

    @Bean
    CommandLineRunner initUsers(
            RoleRepository roleRepo,
            UserRepository userRepo,
            PasswordEncoder encoder) {

        return args -> {

            // ===== ROLES =====
            Role admin = roleRepo.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepo.save(new Role("ROLE_ADMIN")));

            Role etudiant = roleRepo.findByName("ROLE_ETUDIANT")
                    .orElseGet(() -> roleRepo.save(new Role("ROLE_ETUDIANT")));

            Role formateur = roleRepo.findByName("ROLE_FORMATEUR")
                    .orElseGet(() -> roleRepo.save(new Role("ROLE_FORMATEUR")));

            // ===== ADMIN USER =====
            if (userRepo.findByEmail("admin@admin.com").isEmpty()) {

                User u = new User();
                u.setEmail("admin@admin.com");
                u.setPassword(encoder.encode("admin"));
                u.setRole(admin);
                u.setEnabled(true);

                userRepo.save(u);
            }
        };
    }
}

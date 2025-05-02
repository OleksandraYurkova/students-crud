package com.example.crud_spring.config;

import com.example.crud_spring.models.Role;
import com.example.crud_spring.models.User;
import com.example.crud_spring.repository.RoleRepository;
import com.example.crud_spring.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class RoleInitializer {
    RoleRepository roleRepository;
    UserRepository userRepository;
    @Bean
    public CommandLineRunner initRoles(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            Role roleUser = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_USER")));
            if (roleUser == null) {
                roleUser = roleRepository.save(new Role(null, "ROLE_USER"));
            }

            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role(null, "ROLE_ADMIN")));
            if (roleAdmin == null) {
                roleAdmin = roleRepository.save(new Role(null, "ROLE_ADMIN"));
            }

            if (userRepository.findByUsername("admin") == null) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));

                Set<Role> roles = new HashSet<>();
                roles.add(roleAdmin);
                roles.add(roleUser);

                admin.setRoles(roles);
                userRepository.save(admin);
            }
        };
    }
}



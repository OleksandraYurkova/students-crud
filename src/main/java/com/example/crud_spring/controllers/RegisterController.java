package com.example.crud_spring.controllers;

import com.example.crud_spring.AuthResponse;
import com.example.crud_spring.DTO.UserRegistrationDTO;
import com.example.crud_spring.JwtTokenProvider;
import com.example.crud_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final long expirationTimeInMillis = 900_000; // 15 хвилин, або інше значення

    // API: JSON запит → JWT відповідь
    @PostMapping
    public ResponseEntity<?> registerAndGetToken(@RequestBody UserRegistrationDTO dto) {
        System.out.println("Received registration data: " + dto.getUsername() + ", " + dto.getPassword());  // Логування запиту
        userService.registerUser(dto);
        System.out.println("Received user registration: " + dto.getUsername() + ", " + dto.getPassword());
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be null or empty");
        }

        // аутентифікуємо
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(String.valueOf(authentication), expirationTimeInMillis);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
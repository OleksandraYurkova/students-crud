package com.example.crud_spring.controllers;

import com.example.crud_spring.JwtResponse;
import com.example.crud_spring.JwtTokenProvider;
import com.example.crud_spring.RefreshRequest;
import com.example.crud_spring.models.User;
import com.example.crud_spring.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/login")
public class LoginController {

    private final long expirationTimeInMillis = 900_000; // 15 хвилин, або інше значення
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public LoginController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, CustomUserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping
    public String login(@RequestBody User loginRequest) {
        // 1. Аутентифікація користувача
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        // Зберегти результат аутентифікації
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        // Встановити аутентифікацію в контексті безпеки
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Використовувати об'єкт Authentication для генерації токена
        String token = tokenProvider.generateToken(authentication.getName(), expirationTimeInMillis);

        return "Bearer " + token;  // повертаємо токен в заголовку "Authorization"
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model, HttpSession session) {
        String message = (String) session.getAttribute("accessDeniedMessage");
        model.addAttribute("message", message); // передаємо в шаблон
        session.removeAttribute("accessDeniedMessage"); // очищаємо
        return "accessDenied"; // access-denied.html
    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (tokenProvider.validateToken(refreshToken)) {
            String username = tokenProvider.getUsernameFromToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String newAccessToken = tokenProvider.generateAccessToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(newAccessToken, refreshToken));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
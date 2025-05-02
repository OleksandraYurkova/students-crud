package com.example.crud_spring;

import com.example.crud_spring.models.Role;
import com.example.crud_spring.models.User;
import com.example.crud_spring.service.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService; // Для отримання користувача з БД

    private String secretKey = "secret"; // секретний ключ для підпису токену

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = resolveToken(request); // отримуємо токен з запиту
        if (token != null && tokenProvider.validateToken(token)) {
            // Верифікація токену
            Authentication authentication = getAuthentication(token, request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Метод для отримання аутентифікації з токену
    public Authentication getAuthentication(String token, HttpServletRequest request) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenProvider.getKey())  // Потрібно додати метод доступу до ключа в JwtTokenProvider
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject(); // Отримуємо ім'я користувача з токену
        List<String> roles = claims.get("roles", List.class);  // Отримуємо ролі користувача

        // Завантажуємо користувача з БД за ім'ям користувача
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Створюємо аутентифікацію з ролями користувача
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        
        // Використовуємо переданий request
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        
        return authentication;
    }
}
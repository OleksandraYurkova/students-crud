package com.example.crud_spring.controllers;

import com.example.crud_spring.DTO.UserRegistrationDTO;
import com.example.crud_spring.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signin")
public class WebRegisterController {
    UserService userService;

    // Обробка GET запиту для відображення форми
    @GetMapping
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "register";  // повертатиме шаблон register.html
    }

    // Обробка POST запиту для реєстрації
    @PostMapping
    public String registerForm(@ModelAttribute("user") UserRegistrationDTO userRegistrationDTO) {
        userService.registerUser(userRegistrationDTO);
        return "redirect:/login?success";  // перенаправлення на сторінку входу
    }
}

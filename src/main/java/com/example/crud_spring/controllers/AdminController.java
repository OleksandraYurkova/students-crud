package com.example.crud_spring.controllers;

import com.example.crud_spring.DTO.UpdateRoleDTO;
import com.example.crud_spring.models.Role;
import com.example.crud_spring.repository.RoleRepository;
import com.example.crud_spring.repository.UserRepository;
import com.example.crud_spring.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")

public class AdminController {

    private  RoleRepository roleRepository;

    private  UserRepository userRepository;

    private UserService userService;

    public AdminController(RoleRepository roleRepository, UserRepository userRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/")
    public String adminPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll().stream()
                .map(Role::getName)
                .collect(Collectors.toList()));
        return "admin.html"; // ім’я шаблону
    }

    @GetMapping("/roles")
    public List<String> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    @PostMapping("/update-roles")
    public String updateRoles(@ModelAttribute UpdateRoleDTO dto) {
        userService.updateUserRoles(dto.getUserId(), dto.getRoles());
        return "redirect:/admin/";
    }

}

package com.example.crud_spring.controllers;

import com.example.crud_spring.DTO.UpdateRoleDTO;
import com.example.crud_spring.DTO.UserRegistrationDTO;
import com.example.crud_spring.models.Role;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.crud_spring.models.Student;
import com.example.crud_spring.DTO.RequestStudentDTO;
import com.example.crud_spring.DTO.ResponseStudentDTO;
import com.example.crud_spring.models.User;
import com.example.crud_spring.repository.RoleRepository;
import com.example.crud_spring.repository.UserRepository;
import com.example.crud_spring.service.StudentServiceImpl;
import com.example.crud_spring.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final StudentServiceImpl studentService;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;


    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    public HomeController(StudentServiceImpl studentService, UserService userService, PasswordEncoder passwordEncoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.studentService = studentService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

//
//    @GetMapping("/register")
//    public String showRegisterForm(Model model) {
//        model.addAttribute("user", new UserRegistrationDTO());
//        return "register";
//    }
//
//    @PostMapping("/register")
//    public String processRegister(@ModelAttribute("user") UserRegistrationDTO dto) {
//        User user = new User();
//        user.setUsername(dto.getUsername());
//        user.setPassword(passwordEncoder.encode(dto.getPassword()));
//        user.setEnabled(true);
//        Role role = roleRepository.findByName("ROLE_USER")
//                .orElseThrow(() -> new RuntimeException("❌ ROLE_USER not found!"));
//
//        user.setRoles(Set.of(role));
//
//        userRepository.save(user);
//        return "redirect:/login?success";
//    }

    @GetMapping("/students")
    public String showStudents(Model model, Authentication authentication) {
        List<ResponseStudentDTO> students = studentService.getAllStudents().stream()
                .map(student -> new ModelMapper().map(student, ResponseStudentDTO.class))
                .collect(Collectors.toList());
        model.addAttribute("students", students);
        User currentUser = userService.getCurrentUser();
        model.addAttribute("user", currentUser);// Додаємо новий студент для форми

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("newStudent", new RequestStudentDTO());
            return "home";
        } else {
            return "user"; // лише перегляд
        }
    }

    @GetMapping("/home")
    public String home(){
        return "welcome";
    }

//    @GetMapping("/students")
//    public String home(Model model) {
//        List<ResponseStudentDTO> students = studentService.getAllStudents().stream()
//                .map(student -> new ModelMapper().map(student, ResponseStudentDTO.class))
//                .collect(Collectors.toList());
//        model.addAttribute("students", students);
//        model.addAttribute("newStudent", new RequestStudentDTO());
//        User currentUser = userService.getCurrentUser();
//        model.addAttribute("user", currentUser);// Додаємо новий студент для форми
//        return "home";
//    }

    @PostMapping("/students")
    public String createStudent(@ModelAttribute("newStudent") @Valid RequestStudentDTO studentDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("students", studentService.getAllStudents());
            return "home";
        }
        Student student = new ModelMapper().map(studentDTO, Student.class);  // Перетворення DTO в сутність
        studentService.addStudent(student);
        return "redirect:/students";   // Перезавантажуємо сторінку
    }

    @GetMapping("/students/edit/{id}")
    public String editStudentForm(@PathVariable Long id, Model model) {
        Optional<Student> student = studentService.getStudentById(id);
        if (student.isEmpty()) {
            return "redirect:/students";
        }
        model.addAttribute("student", student.get());
        return "editStudent";
    }

    @PostMapping("/students/edit/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute("student") @Valid RequestStudentDTO studentDTO, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "editStudent";
        }
        Student student = new ModelMapper().map(studentDTO, Student.class);
        student.setId(id);
        studentService.updateStudent(student);
        redirectAttributes.addFlashAttribute("success", "Студента успішно оновлено!");
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("success", "Студента успішно видалено!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Студент не знайдений або не можна видалити.");
        }
        return "redirect:/students";
    }

}

package com.example.crud_spring.controllers;

import com.example.crud_spring.models.Student;
import com.example.crud_spring.service.StudentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentController {
    private final StudentServiceImpl service;

    public StudentController(StudentServiceImpl service) {
        this.service = service;
    }


    @GetMapping("/home")
    public String listStudents(Model model) {
        model.addAttribute("students", service.getAllStudents());
        model.addAttribute("newStudent", new Student());
        return "students";
    }

    @PostMapping("/add")
    public String addStudent(@ModelAttribute("newStudent") @Valid Student student, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("error", "Invalid data");
            return "redirect:/home";
        }
        service.addStudent(student);
        return "redirect:/home";
    }
//
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes attributes) {
//        Student student = service.getStudentById(id);
//        if (student == null) {
//            attributes.addFlashAttribute("error", "Student not found.");
//            return "redirect:/students";
//        }
//        model.addAttribute("student", student);
//        return "editStudent";
//    }
//
//    // Обробка оновлення студента
//    @PostMapping("/update")
//    public String updateStudent(@ModelAttribute("student") @Valid Student student, BindingResult result, RedirectAttributes attributes) {
//        if (result.hasErrors()) {
//            return "editStudent"; // Повертаємось до форми, якщо є помилки
//        }
//        service.updateStudent(student);
//        attributes.addFlashAttribute("success", "Student updated successfully.");
//        return "redirect:/students";
//    }
//
//
//    @GetMapping("/delete/{id}")
//    public String deleteStudent(@PathVariable Long id, RedirectAttributes attributes) {
//        try {
//            service.deleteStudent(id);
//        } catch (Exception e) {
//            attributes.addFlashAttribute("error", "Student not found.");
//            return "redirect:/students";
//        }
//        return "redirect:/students";
//    }
}

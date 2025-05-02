package com.example.crud_spring.controllers;

import com.example.crud_spring.models.Student;
import com.example.crud_spring.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/students")
public class StudentRestController {

    private final StudentService studentService;

    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAllStudents() {
        log.info("Отримано запит на отримання всіх студентів");
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Optional<Student> getStudentById(@PathVariable Long id) {
        log.info("Отримано запит на отримання студента з ID: {}", id);
        return studentService.getStudentById(id);
    }


    @PostMapping
    public void addStudent(@RequestBody Student student) {
        log.info("Отримано запит на додавання студента: {}", student);
        studentService.addStudent(student);
    }

    @PutMapping("/{id}")
    public void updateStudent(@PathVariable Long id, @RequestBody Student student) {
        log.info("Отримано запит на оновлення студента: {}", student);
        student.setId(id);
        studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        log.warn("Отримано запит на видалення студента з ID: {}", id);
        studentService.deleteStudent(id);
    }

}

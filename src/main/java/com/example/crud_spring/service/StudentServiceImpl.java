package com.example.crud_spring.service;

import com.example.crud_spring.models.Role;
import com.example.crud_spring.models.Student;
import com.example.crud_spring.models.User;
import com.example.crud_spring.repository.StudentRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;

    @Override
    public List<Student> getAllStudents() {
        log.info("Отримання всіх студентів");
        return studentRepository.findAll();
    }


    @Override
    public Optional<Student> getStudentById(Long id) {
        log.info("Отримання студента з ID: {}", id);
        return studentRepository.findById(id);
    }

    @Override
    public void addStudent(@Valid Student student) {
        log.info("Додавання нового студенту: {}", student);
        studentRepository.add(student);
    }

    @Override
    public void updateStudent(@Valid Student student) {
        log.info("Оновлення студента: {}", student);
        studentRepository.update(student);
    }

    @Override
    public void deleteStudent(Long id) {
        log.warn("Видалення студента з ID: {}", id);
        studentRepository.deleteById(id);
    }

}

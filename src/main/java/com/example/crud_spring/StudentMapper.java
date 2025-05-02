package com.example.crud_spring;

import com.example.crud_spring.DTO.RequestStudentDTO;
import com.example.crud_spring.DTO.ResponseStudentDTO;
import com.example.crud_spring.models.Student;

import java.time.LocalDateTime;

public class StudentMapper {

    // Ручне мапінг для RequestStudentDTO в Student
    public Student toStudent(RequestStudentDTO requestStudentDTO) {
        Student student = new Student();
        student.setName(requestStudentDTO.getName());
        student.setAge(requestStudentDTO.getAge());
        return student;
    }

    // Ручне мапінг для Student в ResponseStudentDTO
    public ResponseStudentDTO toResponseDTO(Student student) {
        ResponseStudentDTO responseDTO = new ResponseStudentDTO();
        responseDTO.setName(student.getName());
        responseDTO.setAge(student.getAge());
        responseDTO.setResponseDate(LocalDateTime.now()); // Додаємо поточну дату
        return responseDTO;
    }
}

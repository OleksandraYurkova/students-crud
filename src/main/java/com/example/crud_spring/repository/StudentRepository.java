
package com.example.crud_spring.repository;

import com.example.crud_spring.models.Student;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepository {  private final List<Student> students = new ArrayList<>();
    private long nextId = 1;
    public List<Student> findAll() {
        return students;
    }

    public Optional<Student> findById(Long id) {
        return students.stream().filter(s -> s.getId().equals(id)).findFirst();
    }

    public void add(Student student) {
        if (student.getId() == null) {
            student.setId(nextId++);  // Призначення id при додаванні студента
        }
        students.add(student);
    }

    public void update(Student student) {
        findById(student.getId()).ifPresent(existingStudent -> {
            existingStudent.setName(student.getName());
            existingStudent.setAge(student.getAge());
        });
    }

    public void deleteById(Long id) {
        students.removeIf(s -> s.getId().equals(id));
    }
}

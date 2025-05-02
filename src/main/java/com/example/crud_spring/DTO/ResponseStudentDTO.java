package com.example.crud_spring.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseStudentDTO {

    private Long id;
    private String name;
    private int age;
    private LocalDateTime responseDate;
}

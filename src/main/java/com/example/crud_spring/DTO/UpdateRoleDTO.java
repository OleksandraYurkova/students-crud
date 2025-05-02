package com.example.crud_spring.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateRoleDTO {
    private Long userId;
    private List<String> roles;

}

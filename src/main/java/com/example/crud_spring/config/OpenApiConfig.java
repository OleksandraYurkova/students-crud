package com.example.crud_spring.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Student API", version = "v1", description = "API для роботи зі студентами"))
public class OpenApiConfig {
}

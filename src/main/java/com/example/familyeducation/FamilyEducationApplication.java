package com.example.familyeducation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.familyeducation.mapper")
public class FamilyEducationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamilyEducationApplication.class, args);
    }

}

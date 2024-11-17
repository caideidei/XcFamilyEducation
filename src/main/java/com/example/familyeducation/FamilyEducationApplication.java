package com.example.familyeducation;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@MapperScan("com.example.familyeducation.mapper")
@EnableTransactionManagement
public class FamilyEducationApplication {

    public static void main(String[] args) {
        SpringApplication.run(FamilyEducationApplication.class, args);
    }

}

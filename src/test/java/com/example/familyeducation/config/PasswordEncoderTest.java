package com.example.familyeducation.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/4 19:20
 **/
@SpringBootTest
public class PasswordEncoderTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testPassword(){
        String rawPassword = "123456"; // 用户输入的明文密码
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("密码加密结果："+encodedPassword);

        //加密后密码无法进行解密，只能通过比对密码的哈希值进行匹配
        String testPassword ="$2a$10$jxPvpw8QbWZGcu4pWhMtDeJ8bk5Ri8Fq9X7P222lTaBjIR.C/eljy";
        boolean matches = passwordEncoder.matches(testPassword, rawPassword);
        System.out.println("密码是否匹配："+matches);
    }

}

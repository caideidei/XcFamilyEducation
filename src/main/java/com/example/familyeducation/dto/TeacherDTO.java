package com.example.familyeducation.dto;

import lombok.Data;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/18 20:38
 **/
@Data
public class TeacherDTO {
    private Long id;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String picture;
    private String role;

    private Long teacherId;
    private String realName;

}

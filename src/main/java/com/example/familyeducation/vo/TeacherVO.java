package com.example.familyeducation.vo;

import lombok.Data;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/18 19:43
 **/

@Data
public class TeacherVO {
    private String username;
    private String phoneNumber;
    private String role;
    private String status;
    private String email;
    private String picture;

    private String subjects;
    private String qualification;
    private String intro;
}

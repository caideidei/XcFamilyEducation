package com.example.familyeducation.dto;

import lombok.Data;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/19 14:36
 **/
@Data
public class ParentDTO {
    private Long id;
    private String username;
    private String password;
    private String phoneNumber;
    private String email;
    private String picture;
    private String status;
    private Long parentId;
    private String realName;
}

package com.example.familyeducation.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/19 18:03
 **/
@Data
public class FeedbackVO {
    private Long id;
    private Long senderId;
    private String message;
    private LocalDateTime createdAt;
    private String username;
    private String role;
    private String picture;
}

package com.example.familyeducation.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserVO {

    private Long id;

    private String username;

    private String phoneNumber;

    private String role;

    private String status;

    private LocalDateTime createdAt;

    private String email;

    private String picture;
}

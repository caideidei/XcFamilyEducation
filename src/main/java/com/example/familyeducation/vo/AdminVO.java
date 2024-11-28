package com.example.familyeducation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/27 19:54
 **/
@Data
public class AdminVO {

    @Schema(name = "id", description = "管理员ID，主键，自增")
    private Long id;

    @Schema(name = "userId", description = "用户ID，外键，指向 users 表的 id")
    private Long userId;

    @Schema(name = "username", description = "用户名，唯一，非空")
    private String username;

    @Schema(name = "realName", description = "管理员真实名字")
    private String realName;

    @Schema(name = "phoneNumber", description = "手机号，唯一，非空")
    private String phoneNumber;

    @Schema(name = "picture", description = "用户头像URL")
    private String picture;

    @Schema(name = "email", description = "用户邮箱")
    private String email;

    @Schema(name = "role", description = "用户角色（admin，teacher，parent）")
    private String role;

    @Schema(name = "status", description = "用户状态（active，inactive，banned），默认 'active'")
    private String status;

    @Schema(name = "createdAt", description = "账户创建时间，默认当前时间")
    private LocalDateTime createdAt;
}

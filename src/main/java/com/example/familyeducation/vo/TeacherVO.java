package com.example.familyeducation.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/18 19:43
 **/

@Data
public class TeacherVO {
    @Schema(name = "id", description = "教师ID，主键，自增")
    private Long id;

    @Schema(name = "userId", description = "用户ID，外键，指向 users 表的 id")
    private Long userId;

    @Schema(name = "username", description = "用户名，唯一，非空")
    private String username;

    @Schema(name = "realName", description = "教师真实姓名，非空")
    private String realName;

    @Schema(name = "phoneNumber", description = "手机号，唯一，非空")
    private String phoneNumber;

    @Schema(name = "role", description = "用户角色（admin，teacher，parent）")
    private String role;

    @Schema(name = "status", description = "用户状态（active，inactive，banned），默认 'active'")
    private String status;

    @Schema(name = "createdAt", description = "账户创建时间，默认当前时间")
    private LocalDateTime createdAt;

    @Schema(name = "email", description = "用户邮箱")
    private String email;

    @Schema(name = "picture", description = "用户头像URL")
    private String picture;

    @Schema(name = "qualification", description = "教师学历或资格证书")
    private String qualification;

    @Schema(name = "intro", description = "教师自我介绍")
    private String intro;

    @Schema(name = "officialTeacher", description = "是否正式教师，默认 false")
    private Integer officialTeacher;

    @Schema(name = "subjects", description = "教师擅长科目，多个科目用逗号分隔")
    private String subjects;
}

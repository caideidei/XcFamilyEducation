package com.example.familyeducation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/12/4 8:36
 **/
@Data
public class AdminDTO {
    @Schema(name="AdminId",description = "管理员id号")
    private Long adminId;

    @Schema(name="id",description = "用户id号")
    private Long id;

    @Schema(name = "username", description = "用户名，唯一，非空")
    private String username;

    @Schema(name = "password", description = "加密后的密码，非空")
    private String password;

    @Schema(name = "phoneNumber", description = "手机号，唯一，非空")
    private String phoneNumber;

    @Schema(name = "role", description = "用户角色（admin，teacher，parent）")
    private String role;

    @Schema(name = "status", description = "用户状态（active，inactive，banned），默认 'active'")
    private String status;

    @Schema(name = "email", description = "用户邮箱")
    private String email;

    @Schema(name = "picture", description = "用户头像URL")
    private String picture;

    @Schema(name = "realName", description = "管理员真实名字")
    private String realName;
}

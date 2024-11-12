package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName(value = "user")
@Schema(name="User",description = "用户信息")
public class User implements Serializable {
    //因为这个类要存数据到Redis中，所以要进行序列化操作，继承Serializable
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO) // 这里设置id为自增
    @Schema(name="id",description = "用户id号")
    private Integer id;

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

    @Schema(name = "createdAt", description = "账户创建时间，默认当前时间")
    private LocalDateTime createdAt;

    @Schema(name = "email", description = "用户邮箱")
    private String email;

    @Schema(name = "picture", description = "用户头像URL")
    private String picture;

}

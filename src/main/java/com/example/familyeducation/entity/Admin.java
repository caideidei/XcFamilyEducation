package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName(value = "admin")
@Schema(name = "Admin", description = "管理员信息")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "管理员ID，主键，自增")
    private Long id;

    @Schema(name = "userId", description = "用户ID，外键，指向 users 表的 id")
    private Long userId;

    @Schema(name = "realName", description = "管理员真实名字")
    private String realName;

    @Schema(name = "lastLoginTime", description = "最近登录时间")
    private LocalDateTime lastLoginTime;

    @Schema(name = "createdAt", description = "管理员账户创建时间，默认当前时间")
    private LocalDateTime createdAt;
}

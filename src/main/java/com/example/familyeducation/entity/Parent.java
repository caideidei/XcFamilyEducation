package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName(value = "parent")
@Schema(name = "Parent", description = "家长信息")
public class Parent implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "家长ID，主键，自增")
    private Long id;

    @TableField(value = "user_id")
    @Schema(name = "userId", description = "用户ID，外键，指向 users 表的 id")
    private Long userId;

    @Schema(name = "realName", description = "家长真实姓名，非空")
    private String realName;

    @Schema(name = "phoneNumber", description = "家长手机号，唯一，非空")
    private String phoneNumber;
}

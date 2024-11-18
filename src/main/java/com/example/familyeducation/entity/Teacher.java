package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName(value = "teacher")
@Schema(name = "Teacher", description = "教师信息")
public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "教师ID，主键，自增")
    private Long id;

    @TableField(value = "user_id")
    @Schema(name = "userId", description = "用户ID，外键，指向 users 表的 id")
    private Long userId;

    @Schema(name = "realName", description = "教师真实姓名，非空")
    private String realName;

    @Schema(name = "qualification", description = "教师学历或资格证书")
    private String qualification;

    @Schema(name = "intro", description = "教师自我介绍")
    private String intro;

    @Schema(name = "officialTeacher", description = "是否正式教师，默认 false")
    private Integer officialTeacher;

    @Schema(name = "subjects", description = "教师擅长科目，多个科目用逗号分隔")
    private String subjects;
}

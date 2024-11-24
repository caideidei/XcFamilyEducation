package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName(value = "teacher_examine")
@Schema(name = "TeacherExamine", description = "教师申请成为正式教师的审核信息")
public class TeacherExamine implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "审核ID，主键，自增")
    private Long id;

    @Schema(name = "teacherId", description = "教师ID，外键，指向 teachers 表的 id")
    private Long teacherId;

    @Schema(name = "adminId", description = "管理员ID，外键，指向 admin 表的 id")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long adminId;

    @Schema(name = "realName", description = "教师真实姓名，非空")
    private String realName;

    @Schema(name = "sex", description = "教师性别")
    private String sex;

    @Schema(name = "qualification", description = "教师学历或资格证书")
    private String qualification;

    @Schema(name = "intro", description = "教师自我介绍")
    private String intro;

    @Schema(name = "subjects", description = "教师擅长科目，多个科目用逗号分隔")
    private String subjects;

    @Schema(name = "status", description = "审核状态（pending: 待审核，approved: 已通过，rejected: 已拒绝），默认 'pending'")
    private String status;

    @Schema(name = "reason", description = "能成为正式教师的原因")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String reason;

    @Schema(name = "createdAt", description = "创建时间，默认当前时间")
    private LocalDateTime createdAt;
}

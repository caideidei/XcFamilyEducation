package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
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

    @Schema(name = "status", description = "审核状态（pending: 待审核，approved: 已通过，rejected: 已拒绝），默认 'pending'")
    private String status;

    @Schema(name = "auditReason", description = "审核失败原因")
    private String auditReason;

    @Schema(name = "reason", description = "能成为正式教师的原因")
    private String reason;

    @Schema(name = "createdAt", description = "创建时间，默认当前时间")
    private LocalDateTime createdAt;
}

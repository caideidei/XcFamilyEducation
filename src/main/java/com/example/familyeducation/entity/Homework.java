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
@TableName(value = "homework")
@Schema(name = "Homework", description = "作业信息")
public class Homework implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "作业ID，主键，自增")
    private Long id;

    @Schema(name = "title", description = "作业标题，非空")
    private String title;

    @Schema(name = "description", description = "作业描述")
    private String description;

    @Schema(name = "deadline", description = "截止时间")
    private LocalDateTime deadline;

    @Schema(name = "fileUrl", description = "作业文件URL")
    private String fileUrl;

    @Schema(name = "status", description = "作业状态，默认 'incompleted'")
    private String status;

    @Schema(name = "createdAt", description = "创建时间，默认当前时间")
    private LocalDateTime createdAt;

    @Schema(name = "parentId", description = "家长ID，外键，指向 parent 表的 id")
    private Long parentId;

    @Schema(name = "teacherId", description = "教师ID，外键，指向 teacher 表的 id")
    private Long teacherId;
}

package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Schema(name = "orderId", description = "订单ID，外键，指向 order 表的 id")
    private Long orderId;

    @Schema(name = "title", description = "作业标题，非空")
    private String title;

    @Schema(name = "description", description = "作业描述")
    private String description;

//    @JsonIgnore
    @Schema(name = "deadline", description = "截止时间")
    private LocalDateTime deadline;

    @Schema(name = "status", description = "作业状态，默认 'incompleted'")
    private String status;

//    @JsonIgnore
    @Schema(name = "createdAt", description = "创建时间，默认当前时间")
    private LocalDateTime createdAt;

    @Schema(name = "fileUrl", description = "提交作业文件URL")
    private String fileUrl;

    @Schema(name = "review", description = "教师反馈")
    private String review;

}

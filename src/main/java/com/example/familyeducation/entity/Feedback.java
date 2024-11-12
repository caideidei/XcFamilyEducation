package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName(value = "feedback")
@Schema(name = "Feedback", description = "反馈信息")
public class Feedback implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "反馈ID，主键，自增")
    private Long id;

    @Schema(name = "senderId", description = "发送者ID，外键，指向 users 表的 id")
    private Long senderId;

    @Schema(name = "message", description = "反馈内容，非空")
    private String message;

    @Schema(name = "createdAt", description = "创建时间，默认当前时间")
    private LocalDateTime createdAt;
}

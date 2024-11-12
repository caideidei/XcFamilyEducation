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
@TableName(value = "notice")
@Schema(name = "Notice", description = "公告信息")
public class Notice implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "公告ID，主键，自增")
    private Long id;

    @Schema(name = "title", description = "公告标题，非空")
    private String title;

    @Schema(name = "content", description = "公告内容，非空")
    private String content;

    @Schema(name = "createdBy", description = "创建者ID，外键，指向 users 表的 id")
    private Long createdBy;

    @Schema(name = "createdAt", description = "创建时间，默认当前时间")
    private LocalDateTime createdAt;

    @Schema(name = "expirationDate", description = "公告过期时间")
    private LocalDateTime expirationDate;
}

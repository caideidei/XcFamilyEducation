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
@TableName(value = "review")
@Schema(name = "Review", description = "家长对教师的评价信息")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "评价ID，主键，自增")
    private Long id;

//    @TableField(value = "parent_id")
    @Schema(name = "parentId", description = "家长ID，外键，指向 parents 表的 id")
    private Long parentId;

    @Schema(name = "rating", description = "评分（1~5分），非空")
    private Integer rating;

    @Schema(name = "review", description = "评价内容")
    private String review;

    @Schema(name = "createdAt", description = "创建时间，默认当前时间")
    private LocalDateTime createdAt;

    @Schema(name = "teacherId", description = "教师ID，外键，指向 teachers 表的 id")
    private Long teacherId;
}

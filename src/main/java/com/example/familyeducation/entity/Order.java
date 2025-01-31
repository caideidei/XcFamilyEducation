package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName(value = "orders")
@Schema(name = "Orders", description = "家教订单信息")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "订单ID，主键，自增")
    private Long id;

    @Schema(name = "teacherId", description = "教师ID，外键，指向 teachers 表的 id")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long teacherId;

    @Schema(name = "parentId", description = "家长ID，外键，指向 parents 表的 id")
    private Long parentId;

    @Schema(name = "status", description = "订单状态，默认 'pendingReview'")
    private String status;

    @Schema(name = "time", description = "家教时间，文字描述")
    private String time;

    @Schema(name = "note", description = "备注")
    private String note;

    @Schema(name = "price", description = "家教价格")
    private Long price;

    @Schema(name = "classNumber", description = "预计课时数")
    private Long classNumber;

    @Schema(name = "nowClassNumber", description = "目前课时数")
    private Long nowClassNumber;

    @Schema(name = "address", description = "家教地址")
    private String address;

    @Schema(name = "grade", description = "学生年级")
    private String grade;

    @Schema(name = "subject", description = "家教科目")
    private String subject;

    @Schema(name = "created_at", description = "发布时间")
//    @JsonIgnore
    private LocalDateTime createdAt;
}

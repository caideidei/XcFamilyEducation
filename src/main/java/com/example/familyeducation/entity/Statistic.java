package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@TableName(value = "statistic")
@Schema(name = "Statistic", description = "平台的整体统计数据")
public class Statistic implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(name = "id", description = "统计ID，主键，自增")
    private Long id;

    @Schema(name = "date", description = "统计日期，非空")
    private LocalDate date;

    @Schema(name = "teacherCount", description = "教师数量，默认 0")
    private Integer teacherCount;

    @Schema(name = "orderCount", description = "订单数量，默认 0")
    private Integer orderCount;

    @Schema(name = "receivedOrderCount", description = "接单数量，默认 0")
    private Integer receivedOrderCount;
}

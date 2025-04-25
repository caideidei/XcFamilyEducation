package com.example.familyeducation.lottery.entity;

import lombok.Data;
/**
 * 奖品实体类
 */

@Data
public class Prize {
    private Long id;
    private String name;
    private Double probability; // 概率，例如 0.05
    private Integer level; // 1=普通奖池，2=高级奖池
}

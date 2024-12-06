package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/12/6 9:29
 **/
@Data
@TableName(value = "saying")
@Schema(name = "Saying", description = "每日一句")
public class Saying {
    private Long id;
    private String saying;
}

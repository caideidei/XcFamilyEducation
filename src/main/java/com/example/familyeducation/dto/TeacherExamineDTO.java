package com.example.familyeducation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/24 14:50
 **/
@Data
public class TeacherExamineDTO {
    private String realName;

    private String sex;

    private String qualification;

    private String intro;

    private String subjects;
}

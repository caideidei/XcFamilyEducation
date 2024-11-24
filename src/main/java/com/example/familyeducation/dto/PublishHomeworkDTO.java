package com.example.familyeducation.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/24 10:01
 **/
@Data
public class PublishHomeworkDTO {

    private Long orderId;

    private String title;

    private String description;

    private LocalDateTime deadline;

}

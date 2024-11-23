package com.example.familyeducation.dto;

import lombok.Data;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/23 8:48
 **/
@Data
public class OrderDTO {

    private String time;

    private String note;

    private Long price;

    private String address;

    private String grade;

    private String subject;
}

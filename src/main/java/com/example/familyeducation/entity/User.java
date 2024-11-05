package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName(value = "user")
public class User implements Serializable {
    //因为这个类要存数据到Redis中，所以要进行序列化操作，继承Serializable
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String phoneNumber;
    private String password;
    private String username;
    private String picture;
    private String role;
}

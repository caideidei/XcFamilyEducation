package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;


@Data
@TableName(value = "user_view")
public class User implements Serializable {
    //因为这个类要存数据到Redis中，所以要进行序列化操作，继承Serializable
    private static final long serialVersionUID = 1L;

    private Integer userId;
    private String userPhone;
    private String userPassword;
    private String userName;
    private String userPicture;
    private String userRole;
}

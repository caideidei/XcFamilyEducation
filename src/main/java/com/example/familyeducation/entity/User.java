package com.example.familyeducation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;

@Data
@TableName(value = "user")
public class User implements Serializable {
    //因为这个类要存数据到Redis中，所以要进行序列化操作，继承Serializable
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO) // 这里设置id为自增
    private Integer id;
    private String username;
    private String password;
    private String phoneNumber;
    private String role;
    private String status;
    private String createdAt;
    private String email;
    private String picture;

}

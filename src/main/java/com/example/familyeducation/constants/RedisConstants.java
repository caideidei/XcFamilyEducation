package com.example.familyeducation.constants;

/**
 * @ClassDescription:Redis的静态变量名
 * @Author:小菜
 * @Create:2024/11/5 17:38
 **/
public class RedisConstants {
    public static final String LOGIN_USER_KEY = "xc:login-jwt:";
    public static final Integer LOGIN_USER_TTL = 120;

    public static final String ORDER_MESSAGE_KEY = "xc:order:";
    public static final Integer ORDER_MESSAGE_TTL = 120;

}

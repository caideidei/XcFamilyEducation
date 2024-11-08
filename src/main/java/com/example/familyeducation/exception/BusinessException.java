package com.example.familyeducation.exception;

/**
 * 自定义异常类，用于业务逻辑中的错误
 */
public class BusinessException extends RuntimeException {

    private int code; // 错误代码
    private String msg; // 错误信息

    // 默认构造器
    public BusinessException(String msg) {
        super(msg);
        this.code = 500; // 默认内部服务器错误
        this.msg = msg;
    }

    // 带错误代码和信息的构造器
    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    // getter 和 setter
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

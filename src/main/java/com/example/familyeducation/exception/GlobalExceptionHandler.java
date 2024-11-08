package com.example.familyeducation.exception;

import com.example.familyeducation.response.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //捕获自定义异常
    @ExceptionHandler(value = {BusinessException.class})
    public ResponseResult<String> handleBusinessException(BusinessException e){
        return new ResponseResult<>(e.getCode(),e.getMsg());
    }

    // 捕获所有的 RuntimeException 异常
    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseResult<String> handleRuntimeException(RuntimeException e) {
        // 这里会捕获到一些业务异常，或者其他运行时异常
        return new ResponseResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "运行时异常: " + e.getMessage());
    }

    // 捕获所有其他的 Exception 异常
    @ExceptionHandler(value = {Exception.class})
    public ResponseResult<String> handleException(Exception e) {
        // 处理一些无法捕获的异常
        return new ResponseResult<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "系统异常: " + e.getMessage(),null);
    }
}


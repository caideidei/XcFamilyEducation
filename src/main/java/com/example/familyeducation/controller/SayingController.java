package com.example.familyeducation.controller;

import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.SayingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/12/6 9:32
 **/
@RestController
@RequestMapping("/saying")
public class SayingController {
    @Autowired
    private SayingService sayingService;

    @GetMapping("/selectSaying")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseResult selectSaying(){
        int value = LocalDate.now().getDayOfWeek().getValue();
        String saying = sayingService.selectById(value);
        if(saying!=null){
            return ResponseResult.success("成功查询数据", saying);
        }else{
            return ResponseResult.error("查询好句失败，但也要好好努力哦！");
        }
    }
}

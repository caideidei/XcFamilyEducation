package com.example.familyeducation.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Tag(name = "test", description = "test")
public class HelloController {

    @GetMapping("/t")
    public String t1(){
        return "111";
    }

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping("/ok")
    public String ok(){
        return "ok";
    }

    @RequestMapping("/ok/t1")
    public int test1(){
        List<Integer> integers = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            integers.add(i);
        }
        //故意数组过界进行异常测试
        for (int i = 0; i < 20; i++){
            System.out.println(integers.get(i));
        }
        return 1;
    }

    @RequestMapping("/ok/t2")
    public int test2(){
        int t = 10/0;
        System.out.println(t);
        return 1;
    }


}

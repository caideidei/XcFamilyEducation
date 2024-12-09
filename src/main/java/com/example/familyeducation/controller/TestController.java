package com.example.familyeducation.controller;

import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.utils.AliOSSUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "测试接口", description = "管理测试信息")
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @PostMapping("/oss/upload")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public ResponseResult upload(MultipartFile file){
        log.info("文件上传：{}",file);

        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //截取原始文件名的后缀   dfdfdf.png
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名称（ 防止存在阿里云的文件名重复，会被新的覆盖）
            String objectName = UUID.randomUUID().toString() + extension;
            //文件的请求路径
            String filePath = aliOSSUtils.upload(file.getBytes(), objectName);
            return ResponseResult.success("文件上传成功，路径如下：",filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        return ResponseResult.error("文件上传失败");
    }



    //测试权限功能
    @GetMapping("/t")
    public String t1(){
        return "111";
    }

    @GetMapping("/hello")
    @PreAuthorize("hasRole('ADMIN')")
    public String hello(){
        return "hello";
    }

    @GetMapping("/ok")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public String ok(){
        return "ok";
    }

    //测试异常处理
    @GetMapping("/ok/t1")
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

    @GetMapping("/ok/t2")
    public int test2(){
        int t = 10/0;
        System.out.println(t);
        return 1;
    }

    @GetMapping("/holder")
    public ResponseResult holder(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //authentication:UsernamePasswordAuthenticationToken [Principal=LoginUser(user=User(id=3, username=小菜管理员,
        // password=$2a$10$Z9aDmES148Yl1eVfmyYAXOMCet4Cb4tbdqY5Jix25NCnYsubhY3bO, phoneNumber=123456, role=admin,
        // status=active, createdAt=2024-11-05 16:40:53, email=123456@qq.com, picture=null), redisAuthorities=[ROLE_ADMIN],
        // authorities=[ROLE_ADMIN]), Credentials=[PROTECTED], Authenticated=true, Details=null, Granted Authorities=[ROLE_ADMIN]]
        LoginUser user = (LoginUser) authentication.getPrincipal();
        //user:LoginUser(user=User(id=3, username=小菜管理员, password=$2a$10$Z9aDmES148Yl1eVfmyYAXOMCet4Cb4tbdqY5Jix25NCnYsubhY3bO,
        // phoneNumber=123456, role=admin, status=active, createdAt=2024-11-05 16:40:53, email=123456@qq.com, picture=null),
        // redisAuthorities=[ROLE_ADMIN], authorities=[ROLE_ADMIN])
        return new ResponseResult(200,"user:"+user);
    }

}

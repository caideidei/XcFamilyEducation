package com.example.familyeducation.controller;

import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/12/4 9:48
 **/
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/oss/upload")
    public ResponseResult upload(MultipartFile file){
        log.info("文件上传：{}",file);
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID().toString() + extension;
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return ResponseResult.success("文件上传成功，路径如下：",filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        return ResponseResult.error("文件上传失败");
    }
}

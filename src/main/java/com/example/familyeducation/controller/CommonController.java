package com.example.familyeducation.controller;

import cn.hutool.core.util.RandomUtil;
import com.aliyun.oss.model.ObjectMetadata;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.CommonService;
import com.example.familyeducation.utils.AliOSSUtils;
import com.example.familyeducation.utils.AliSMSUtils;
import com.example.familyeducation.utils.RedisCache;
import com.example.familyeducation.utils.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.example.familyeducation.constants.RedisConstants.PHONE_CODE_KEY;
import static com.example.familyeducation.constants.RedisConstants.PHONE_CODE_TTL;

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
    private AliOSSUtils aliOSSUtils;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private CommonService commonService;

    @PostMapping("/oss/upload")
    public ResponseResult upload(MultipartFile file){
        log.info("文件上传：{}",file);
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID().toString() + extension;

            // 设置文件元数据
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(getcontentType(extension));

            String filePath = aliOSSUtils.upload(file.getBytes(), objectName,metadata);
            return ResponseResult.success("文件上传成功，路径如下：",filePath);
        } catch (IOException e) {
            log.error("文件上传失败：{}", e);
        }
        return ResponseResult.error("文件上传失败");
    }

    @GetMapping("/sendMessage")
    public ResponseResult sendMessage(@RequestParam String phone){
        //1.校验手机号是否符合手机号
        if(RegexUtils.isPhoneInvalid(phone)){
            return ResponseResult.error("请输入正确的手机号");
        }
        //2.生成验证码
        String code = RandomUtil.randomNumbers(6);
        //3.保存验证码到Redis中
        redisCache.setCacheObject(PHONE_CODE_KEY+phone,code,PHONE_CODE_TTL, TimeUnit.MINUTES);
        //4.发送验证码
        boolean flag = AliSMSUtils.sendMessage(phone,code);
        //5.返回信息
        if(flag) {
            return ResponseResult.success("成功发送验证码", null);
        }else{
            return ResponseResult.error("发送验证码失败");
        }
    }

    @PostMapping("/refreshToken")
    public ResponseResult refreshToken(@RequestHeader String refreshToken){
        String token = commonService.refreshToken(refreshToken);
        if(token==null){
            return ResponseResult.error("刷新token失败");
        }else{
            return ResponseResult.success("刷新token成功",token);
        }
    }

    public static String getcontentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase(".bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase(".gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase(".jpeg") ||
                FilenameExtension.equalsIgnoreCase(".jpg") ||
                FilenameExtension.equalsIgnoreCase(".png")) {
            return "image/jpg";
        }
        if (FilenameExtension.equalsIgnoreCase(".html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase(".txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase(".vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase(".pptx") ||
                FilenameExtension.equalsIgnoreCase(".ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase(".docx") ||
                FilenameExtension.equalsIgnoreCase(".doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase(".xml")) {
            return "text/xml";
        }
        return "image/jpg";
    }

}

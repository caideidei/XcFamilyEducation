package com.example.familyeducation.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

/**
 * OSS初始化配置
 */
@Log4j2
@Configuration
public class OssConfig {
    /**
     * 配置文件中读取阿里云 OSS 的 endpoint，注入到 endPoint 变量中
     */
    @Value("${aliyun.oss.endPoint}")
    private String endPoint;

    /**
     * 从配置文件中读取阿里云 OSS 的 accessKeyId，注入到 accessKeyId 变量中
     */
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;

    /**
     * 从配置文件中读取阿里云 OSS 的 accessKeySecret，注入到 accessKeySecret 变量中
     */
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;

    private OSS ossClient;

    @Bean
    public OSS init() {
        // 如果 OSS 客户端尚未初始化，则进行初始化
        if (ossClient == null) {
            // 使用 OSSClientBuilder 构建 OSS 客户端，传入 endpoint、accessKeyId 和 accessKeySecret
            ossClient = createOSSClient();

            // 记录日志，表示连接成功
            log.info("OSS服务连接成功！");
        }
        // 返回初始化好的 OSS 客户端实例
        return ossClient;
    }

    /**
     * 创建 OSS 客户端的方法
     */
    private OSS createOSSClient() {
        return new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
    }

    @PreDestroy
    public void destroy() {
        // 关闭 OSS 客户端
        if (ossClient != null) {
            // 调用 shutdown() 方法关闭 OSS 客户端
            ossClient.shutdown();

            // 记录日志，确认客户端已成功关闭
            log.info("OSS客户端已成功关闭。");
        }
    }
}

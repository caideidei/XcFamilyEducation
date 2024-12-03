package com.example.familyeducation.utils;

import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.example.familyeducation.config.OssConfig;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * OSS 文件管理服务
 */
@Log4j2
@Component
public class OssUtil {

    /** 自动注入 OssConfig 类型的 Bean */
    @Autowired
    private OssConfig ossConfig;

    /** 定义访问前缀，用于构建文件的完整访问路径 */
    @Value("${aliyun.oss.accessPre}")
    private String accessPre;

    /** 定义存储桶名称，方便在上传和下载时引用 */
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    /**
     * 默认路径上传本地文件
     *
     * @param filePath 本地文件路径
     * @return 上传后的文件访问路径
     */
    public String uploadFile(String filePath) {
        return uploadFileForBucket(bucketName, getOssFilePath(filePath), filePath);
    }

    /**
     * 默认路径上传 MultipartFile 文件
     *
     * @param multipartFile 待上传的文件
     * @return 上传后的文件访问路径
     */
    public String uploadMultipartFile(MultipartFile multipartFile) {
        return uploadMultipartFile(bucketName, getOssFilePath(multipartFile.getOriginalFilename()), multipartFile);
    }

    /**
     * 上传 MultipartFile 类型文件到指定 Bucket
     *
     * @param bucketName    实例名称
     * @param ossPath       OSS 存储路径
     * @param multipartFile 待上传的文件
     * @return 上传后的文件访问路径
     */
    public String uploadMultipartFile(String bucketName, String ossPath, MultipartFile multipartFile) {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            uploadFileInputStreamForBucket(bucketName, ossPath, inputStream);
        } catch (IOException e) {
            log.error("上传文件失败: {}", e.getMessage(), e);
            return null;
        }
        return accessPre + "/" + ossPath;  // 确保 accessPre 和 ossPath 之间有 "/"
    }

    /**
     * 使用 File 上传文件
     *
     * @param bucketName 实例名称
     * @param ossPath    OSS 存储路径
     * @param filePath   本地文件路径
     * @return 上传后的文件访问路径
     */
    public String uploadFileForBucket(String bucketName, String ossPath, String filePath) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, ossPath, new File(filePath));
        ossConfig.init().putObject(putObjectRequest);
        return accessPre + "/" + ossPath;  // 确保 accessPre 和 ossPath 之间有 "/"
    }

    /**
     * 使用文件流上传到指定的 Bucket 实例
     *
     * @param bucketName  实例名称
     * @param ossPath     OSS 存储路径
     * @param inputStream 文件输入流
     */
    public void uploadFileInputStreamForBucket(String bucketName, String ossPath, InputStream inputStream) {
        ossConfig.init().putObject(bucketName, ossPath, inputStream);
    }

    /**
     * 下载文件
     *
     * @param ossFilePath OSS 存储路径
     * @param filePath    本地文件路径
     */
    public void downloadFile(String ossFilePath, String filePath) {
        downloadFileForBucket(bucketName, ossFilePath, filePath);
    }

    /**
     * 从指定 Bucket 下载文件
     *
     * @param bucketName  实例名称
     * @param ossFilePath OSS 存储路径
     * @param filePath    本地文件路径
     */
    public void downloadFileForBucket(String bucketName, String ossFilePath, String filePath) {
        ossConfig.init().getObject(new GetObjectRequest(bucketName, ossFilePath), new File(filePath));
    }

    /**
     * 获取默认 OSS 存储路径
     *
     * @return 默认 OSS 存储路径
     */
    public String getOssDefaultPath() {
        LocalDateTime now = LocalDateTime.now();
        return String.format("%d/%d/%d/%d/%d/",
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                now.getHour(),
                now.getMinute());
    }

    /**
     * 生成 OSS 文件路径
     *
     * @param filePath 本地文件路径
     * @return OSS 文件路径
     */
    public String getOssFilePath(String filePath) {
        // 获取文件后缀
        String fileSuffix = filePath.substring(filePath.lastIndexOf(".") + 1);

        // 确保路径部分没有拼接错误，保证路径之间有"/"
        String defaultPath = getOssDefaultPath();
        if (!defaultPath.endsWith("/")) {
            defaultPath += "/"; // 如果末尾没有 "/"，则手动添加
        }

        // 生成唯一的文件路径
        return defaultPath + UUID.randomUUID().toString() + "." + fileSuffix;
    }
}

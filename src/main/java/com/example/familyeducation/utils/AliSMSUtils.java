package com.example.familyeducation.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/12/9 20:07
 **/
@Component
public class AliSMSUtils {

    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyIdConfig;

    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecretConfig;

    @Value("${aliyun.sms.signName}")
    private String signNameConfig;

    @Value("${aliyun.sms.templateCode}")
    private String templateCodeConfig;

    private static String accessKeyId;
    private static String accessKeySecret;
    private static String signName;
    private static String templateCode;

    @PostConstruct
    public void init() {
        accessKeyId = accessKeyIdConfig;
        accessKeySecret = accessKeySecretConfig;
        signName = signNameConfig;
        templateCode = templateCodeConfig;
    }

    public static boolean sendMessage(String phone, String code) {

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);

        SendSmsRequest request = new SendSmsRequest();
        request.setSignName(signName);
        request.setTemplateCode(templateCode);
        request.setPhoneNumbers(phone);
        request.setTemplateParam("{\"code\":\"" + code + "\"}");

        try {
            SendSmsResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (com.aliyuncs.exceptions.ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
            return false;
        }
        return true;
    }
}

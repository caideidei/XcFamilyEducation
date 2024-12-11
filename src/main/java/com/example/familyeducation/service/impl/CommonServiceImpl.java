package com.example.familyeducation.service.impl;

import com.example.familyeducation.service.CommonService;
import com.example.familyeducation.utils.JwtUtil;
import com.example.familyeducation.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.familyeducation.constants.RedisConstants.REFRESH_CODE_KEY;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/12/10 21:01
 **/
@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private RedisCache redisCache;

    @Override
    public String refreshToken(String refreshToken) {
        //1.检验refreshToken是否过期
        String userId;
        try {
            userId = JwtUtil.parseJWT(refreshToken).getSubject();
        } catch (Exception e) {
            throw new RuntimeException("refreshToken已过期");
        }
        //2.refreshToken不过期，检验Redis是否存在token
        String key = REFRESH_CODE_KEY+userId;
        String redisRefreshToken = redisCache.getCacheObject(key);
        //2.1不存在，报错
        if(!redisRefreshToken.equals(refreshToken)){
            throw new RuntimeException("refreshToken不匹配");
        }
        //3.存在，生成新token并返回
        String newToken = JwtUtil.createJWT(userId);
        return newToken;
    }
}

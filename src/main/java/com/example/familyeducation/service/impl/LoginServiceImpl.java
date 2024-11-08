package com.example.familyeducation.service.impl;

import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.service.LoginService;
import com.example.familyeducation.utils.JwtUtil;
import com.example.familyeducation.utils.RedisCache;
import com.example.familyeducation.response.ResponseResult;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;

import static com.example.familyeducation.constants.RedisConstants.LOGIN_USER_KEY;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/5 17:03
 **/
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {
        //1.构造用户名密码认证信息
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        //2.使用SpringSecurity 中用于封装用户名密码认证信息的UsernamePasswordAuthenticationToken来进行认证
        //这里会进行账号密码校验，不成功会报403
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //3.认证不通过报错
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }
        //4.认证通过则生成token
        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String token = JwtUtil.createJWT(userId);//将userId进行token生成
        //5.封装数据到Redis中
        redisCache.setCacheObject(LOGIN_USER_KEY+userId,loginUser);
        //6.最后将token返回前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token:",token);
        return new ResponseResult(200,"登录成功",map);
    }

    @Override
    public ResponseResult logout() {
        //1.从SecurityContextHolder中查找到Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //2.从Authentication中获取LoginUser
        LoginUser loginUser  = (LoginUser) authentication.getPrincipal();
        //3.获取id并从Redis中删除，那样下次再进行过滤时就查询不到Redis中的数据，显示用户未登录
        Integer userId = loginUser.getUser().getId();
        redisCache.deleteObject(LOGIN_USER_KEY+userId);
        return new ResponseResult(200,"成功退出登录");
    }
}

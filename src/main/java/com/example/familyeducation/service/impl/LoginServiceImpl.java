package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.UserPhoneCodeDTO;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.Parent;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.ParentMapper;
import com.example.familyeducation.mapper.TeacherMapper;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.service.LoginService;
import com.example.familyeducation.service.UserService;
import com.example.familyeducation.utils.*;
import com.example.familyeducation.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.example.familyeducation.constants.RedisConstants.*;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private ParentMapper parentMapper;

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult login(User user) {
        //1.构造用户名密码认证信息
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getPhoneNumber(), user.getPassword());
        //2.使用SpringSecurity 中用于封装用户名密码认证信息的UsernamePasswordAuthenticationToken来进行认证
        //这里会进行账号密码校验，不成功会报403
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //3.认证不通过报错
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }
        //4.认证通过则生成双token
        LoginUser loginUser= (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
//        String role = loginUser.getUser().getRole();

        String token = JwtUtil.createJWT(userId);//将userId进行token生成
        String refreshToken = JwtUtil.createRefreshToken(userId);//生成长token
        //4.1如果用户被禁用则无法登录
        String userStatus = userService.selectUserStatusByUserId(Long.valueOf(userId));
        if(userStatus.equals("banned")){
            return ResponseResult.error("您已被禁用，无法登录");
        }
        //5.封装数据到Redis中
        redisCache.setCacheObject(LOGIN_USER_KEY+userId,loginUser,LOGIN_USER_TTL, TimeUnit.MINUTES);
        redisCache.setCacheObject(REFRESH_CODE_KEY+userId,refreshToken,REFRESH_CODE_TTL,TimeUnit.MINUTES);
        //6.最后将token返回前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token",token);
        map.put("refreshToken",refreshToken);
        return new ResponseResult(200,"登录成功",map);
    }

    @Override
    public ResponseResult loginByPhoneNumber(UserPhoneCodeDTO userPhoneCodeDTO) {
        //1.判断数据是否非空
        if(!ValidationUtil.areAllFieldsNonNull(userPhoneCodeDTO)){
            //1.1为空报错
            return ResponseResult.error("数据填写不完整");
        }
        //2.从Redis中去手机号对应的验证码
        String phoneNumber = userPhoneCodeDTO.getPhoneNumber();
        String inputCode = userPhoneCodeDTO.getPhoneCode();
        String redisCode =  redisCache.getCacheObject(PHONE_CODE_KEY+phoneNumber);
        //2.1验证码不对应，报错
        if(!inputCode.equals(redisCode)){
            return ResponseResult.error("验证码不正确，请重试");
        }
        //3.验证码对应，查询用户userId和role
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number",phoneNumber);
        User user = userMapper.selectOne(queryWrapper);
        Long userId = user.getId();
        String role = user.getRole();
        //3.1生成token并保存到Redis中
        String token = JwtUtil.createJWT(String.valueOf(userId));
        //4.1如果用户被禁用则无法登录
        String userStatus = userService.selectUserStatusByUserId(Long.valueOf(userId));
        if(userStatus.equals("banned")){
            return ResponseResult.error("您已被禁用，无法登录");
        }
        //4.2封装对象为loginUser
        List<String> list = new ArrayList<>();//这里list就是LoginUser中的redisAuthorities
        if(role.equals("admin")){
            list.add("ROLE_ADMIN");//注意这里添加自定义权限时要加前缀ROLE_，SpringSecurity会默认根据ROLE_去查找权限
        } else if (role.equals("teacher")) {
            list.add("ROLE_TEACHER");
        }else{
            list.add("ROLE_PARENT");
        }
        LoginUser loginUser = new LoginUser(user, list);
        //5.封装数据到Redis中
        redisCache.setCacheObject(LOGIN_USER_KEY+userId,loginUser,LOGIN_USER_TTL, TimeUnit.MINUTES);
        //6.最后将token返回前端
        HashMap<String, String> map = new HashMap<>();
        map.put("token",token);
        return new ResponseResult(200,"登录成功",map);
    }


    @Override
    public ResponseResult logout() {
        //1.从SecurityContextHolder中查找到Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //2.从Authentication中获取LoginUser
        LoginUser loginUser  = (LoginUser) authentication.getPrincipal();
        //3.获取id并从Redis中删除，那样下次再进行过滤时就查询不到Redis中的数据，显示用户未登录
        Long userId = loginUser.getUser().getId();
        redisCache.deleteObject(LOGIN_USER_KEY+userId);
        return new ResponseResult(200,"成功退出登录");
    }

    @Override
    @Transactional
    public ResponseResult register(User user) {
        //1.首先对传入的参数(用户名，手机号，密码，角色)进行非空判断
        String username = user.getUsername();
        String phoneNumber = user.getPhoneNumber();
        String rawPassword = user.getPassword();
        String role = user.getRole();
        //密码加密
        String password = passwordEncoder.encode(rawPassword);
        user.setPassword(password);
        int insertRegisterNumber = 0;//用来判断插入数据条数
        if(username==null||phoneNumber==null||password==null||role==null||
        StringUtils.isEmpty(username) ||StringUtils.isEmpty(phoneNumber)||StringUtils.isEmpty(password)||StringUtils.isEmpty(role)){
            return ResponseResult.error("数据填写不完整，注册失败");
        }
        //2.判断手机号是否已经注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number",phoneNumber);
        List<User> userList = userMapper.selectList(queryWrapper);
        if(!userList.isEmpty()){
            //2.1.手机号已经注册则报错
            return ResponseResult.error("该手机号已被注册");
        }else{
            //TODO 对用户手机号进行检验（使用手机验证码进行匹配，匹配不成功则不能注册成功）
            //3.未注册则进行注册，将用户信息存入数据库
            //TODO事务插入数据
            //3.1将用户数据存入user表中
            userMapper.insert(user);
            //3.2根据role将数据存入teacher/parent表中
            //这里要先获取user表中的用户id才能向teacher/parent表中插入数据
            QueryWrapper<User> queryRegisterWrapper = new QueryWrapper<>();
            queryRegisterWrapper.eq("phone_number",phoneNumber);
            User registerUser = userMapper.selectOne(queryRegisterWrapper);
            Long registerUserId = registerUser.getId();
            String registerUserUsername = registerUser.getUsername();
            if(role.equals("teacher")){
                Teacher teacher = new Teacher();
                teacher.setUserId(registerUserId);
                teacher.setRealName(registerUserUsername);
                insertRegisterNumber = teacherMapper.insert(teacher);
            }else{
                Parent parent = new Parent();
                parent.setUserId(registerUserId);;
                parent.setRealName(registerUserUsername);
                insertRegisterNumber = parentMapper.insert(parent);
            }
        }
        //4.判断插入数据，返回结果
        if(insertRegisterNumber==1){
            return ResponseResult.success("注册成功",null);
        }else{
            return ResponseResult.error("注册失败");
        }
    }


}

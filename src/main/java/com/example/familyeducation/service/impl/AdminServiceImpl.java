package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.UserDTO;
import com.example.familyeducation.entity.Admin;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.AdminMapper;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.AdminService;
import com.example.familyeducation.utils.RedisCache;
import com.example.familyeducation.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.familyeducation.constants.RedisConstants.LOGIN_USER_KEY;

/**
 * @ClassDescription:管理员相关接口
 * @Author:小菜
 * @Create:2024/11/11 16:23
 **/
@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisCache redisCache;

    /**
     * @author 小菜
     * @date  2024/11/18
     * @description 查询所有管理员信息
     **/
    @Override
    public ResponseResult selectAllAdmins() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role","admin");
        List<User> admins = userMapper.selectList(queryWrapper);

        //将查询到的数据封装为UserVO
        List<UserVO> userVOS = admins.stream().map(user -> {
            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setUsername(user.getUsername());
            userVO.setPhoneNumber(user.getPhoneNumber());
            userVO.setRole(user.getRole());
            userVO.setStatus(user.getStatus());
            userVO.setCreatedAt(user.getCreatedAt());
            userVO.setEmail(user.getEmail());
            userVO.setPicture(user.getPicture());
            return userVO;
        }).collect(Collectors.toList());
        return new ResponseResult(200,"查询成功",userVOS);
    }

    /**
     * @author 小菜
     * @date  2024/11/18
     * @description 插入新管理员信息
     **/
    @Override
    @Transactional
    public ResponseResult insertAdmin(UserDTO userDTO) {
        int insertAdminNumber = 0;//用来判断后续数据是否插入成功
        //1.封装user数据
        //1.1创建一个User对象并将userDTO中数据传给user
        User user = new User();
        BeanUtils.copyProperties(userDTO,user);
        //1.2添加user其他数据
        //初始密码是手机号
        String phoneNumber = userDTO.getPhoneNumber();
        String password = passwordEncoder.encode(phoneNumber);
        user.setPassword(password);
        user.setRole("admin");
        //2.判断手机号是否已被注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number",phoneNumber);
        List<User> userList = userMapper.selectList(queryWrapper);
        if(!userList.isEmpty()){
            //2.1已被注册则报错
            return ResponseResult.error("该手机号已被注册");
        }else{
            //3.未注册则将数据插入数据库
            // 3.1先插入user表中
            userMapper.insert(user);
            //3.2再获取user表中的数据并封装admin对象
            QueryWrapper<User> queryRegisterWrapper = new QueryWrapper<>();
            queryRegisterWrapper.eq("phone_number",phoneNumber);
            User registerUser = userMapper.selectOne(queryRegisterWrapper);
            Long registerUserId = registerUser.getId();
            LocalDateTime createdAt = registerUser.getCreatedAt();

            Admin admin = new Admin();
            admin.setUserId(registerUserId);
            admin.setCreatedAt(createdAt);
            insertAdminNumber = adminMapper.insert(admin);
        }
        //4.判断插入是否成功并返回信息
        if(insertAdminNumber==0){
            return ResponseResult.error("新增管理员失败");
        }else{
            return ResponseResult.success("新增管理员成功",null);
        }

    }

    /**
     * @author 小菜
     * @date  2024/11/18
     * @description 根据登录管理员id更新管理员信息
     **/
    @Override
    public ResponseResult updateAdmin(User user) {
        //1.1先从Holder中获取当前登录管理员id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        user.setId(loginUserId);
        //2.根据获取到的id来修改管理员信息保证修改的只能是当前登录管理员的信息
        //2.1判断封装的数据是否满足要求（账号唯一、非空等），不满足报错
        //首先对传入的参数(用户名，手机号，密码，角色)进行非空判断
        String username = user.getUsername();
        String phoneNumber = user.getPhoneNumber();
        String rawPassword = user.getPassword();
        String role = user.getRole();
        //密码加密
        String password = passwordEncoder.encode(rawPassword);
        user.setPassword(password);
        int updateAdminNumber = 0;//用来判断插入数据条数
        if(username==null||phoneNumber==null||password==null||role==null||
                StringUtils.isEmpty(username) ||StringUtils.isEmpty(phoneNumber)||StringUtils.isEmpty(password)||StringUtils.isEmpty(role)){
            return ResponseResult.error("数据填写不完整，修改管理员信息失败");
        }
        //2.判断手机号是否已经注册(这里查询手机号是如果用户是自己是可以的，相当于修改自己的其他信息，手机号不变)
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number",phoneNumber).ne("id",loginUserId);
        List<User> userList = userMapper.selectList(queryWrapper);
        if(!userList.isEmpty()){
            //2.1.手机号已经注册则报错
            return ResponseResult.error("该手机号已被注册");
        }else{
            //4.满足要求将数据存入数据库中
            updateAdminNumber = userMapper.updateById(user);
        }
        //5.判断是否修改成功并返回信息
        if(updateAdminNumber==0){
            return ResponseResult.error("修改管理员信息失败");
        }else{
            return ResponseResult.success("修改管理员信息成功",null);
        }

    }

    /**
     * @author 小菜
     * @date  2024/11/18
     * @description 删除对应的管理员信息
     **/
    @Override
    public ResponseResult deleteAdmin(String phoneNumber) {
        int deleteAdminNumber = 0;//判断删除数量
        //1.根据手机号查询信息
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number",phoneNumber);
        User user = userMapper.selectOne(queryWrapper);
        Long userId = user.getId();
        //2.判断该管理员状态
        String status = user.getStatus();
        if(!status.equals("banned")){
            //2.1不为banned时无法删除，报错
            return ResponseResult.error("无法删除该管理员");
        }else{
            //3.为banned时可以删除，删除数据库中的信息
            //3.1删除user表中的信息,admin中的信息会自动删除
            deleteAdminNumber = userMapper.deleteById(userId);
            //TODO删除管理员后Redis中删除数据
            redisCache.deleteObject(LOGIN_USER_KEY+userId);
        }

        //4.根据删除情况返回信息
        if(deleteAdminNumber==0){
            return ResponseResult.error("删除管理员失败");
        }else{
            return ResponseResult.success("删除管理员成功",null);
        }
    }
}

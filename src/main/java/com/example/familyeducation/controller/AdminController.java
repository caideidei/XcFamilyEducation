package com.example.familyeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.AdminDTO;
import com.example.familyeducation.dto.UserDTO;
import com.example.familyeducation.entity.Admin;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.AdminService;
import com.example.familyeducation.service.UserService;
import com.example.familyeducation.utils.RedisCache;
import com.example.familyeducation.vo.AdminVO;
import com.example.familyeducation.vo.UserInfoVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.familyeducation.constants.RedisConstants.LOGIN_USER_KEY;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/11 16:21
 **/
@RestController
@RequestMapping("/admin")
@Tag(name="管理员接口",description = "管理管理员信息")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisCache redisCache;

    @GetMapping("/selectAllAdmins")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult selectAllAdmins(){
        List<AdminVO> adminVOList = adminService.selectAllAdmins();
        if(adminVOList.isEmpty()){
            return ResponseResult.success("查询数据为空",null);
        }else{
            return ResponseResult.success("查询成功",adminVOList);
        }
    }

    @PostMapping("/insertAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseResult insertAdmin(@RequestBody UserDTO userDTO){
        int insertAdminNumber = 0;//用来判断后续数据是否插入成功
        int insertUserNumber = 0;
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
        List<User> userList= userService.selectByPhone(phoneNumber);
        if(!userList.isEmpty()){
            //2.1已被注册则报错
            return ResponseResult.error("该手机号已被注册");
        }else{
            //3.未注册则将数据插入数据库
            // 3.1先插入user表中
            insertUserNumber = userService.insertUser(user);
            //3.2再获取user表中的数据并封装admin对象
            User registerUser = userService.selectOneByPhone(phoneNumber);
            Long registerUserId = registerUser.getId();
            LocalDateTime createdAt = registerUser.getCreatedAt();

            Admin admin = new Admin();
            admin.setUserId(registerUserId);
            admin.setCreatedAt(createdAt);
            insertAdminNumber = adminService.insertAdmin(admin);
        }
        //4.判断插入是否成功并返回信息
        if(insertAdminNumber==0||insertUserNumber==0){
            return ResponseResult.error("新增管理员失败");
        }else{
            return ResponseResult.success("新增管理员成功",null);
        }
    }

    @PutMapping("/updateAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseResult updateAdmin(@RequestBody AdminDTO adminDTO){
        //1.1先从Holder中获取当前登录管理员id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        //封装数据
        User user = new User();
        BeanUtils.copyProperties(adminDTO,user);
        Admin admin = new Admin();
        admin.setId(adminDTO.getAdminId());
        admin.setUserId(user.getId());
        admin.setRealName(adminDTO.getRealName());
        //2.根据获取到的id来修改管理员信息保证修改的只能是当前登录管理员的信息
        if(loginUserId!=user.getId()){
            return ResponseResult.error("无法修改别人的信息");
        }
        //2.1判断封装的数据是否满足要求（账号唯一、非空等），不满足报错
        //首先对传入的参数(用户名，手机号，密码，角色)进行非空判断
        String username = user.getUsername();
        String phoneNumber = user.getPhoneNumber();
        String rawPassword = user.getPassword();
        String role = user.getRole();
        //密码加密
        String password = passwordEncoder.encode(rawPassword);
        user.setPassword(password);
        int updateUserNumber = 0;//用来判断插入数据条数
        int updateAdminNumber = 0;
        //判断非空
        if(username==null||phoneNumber==null||password==null||role==null||
                StringUtils.isEmpty(username) ||StringUtils.isEmpty(phoneNumber)||StringUtils.isEmpty(password)||StringUtils.isEmpty(role)){
            return ResponseResult.error("数据填写不完整，修改管理员信息失败");
        }
        //3.判断手机号是否已经注册(这里查询手机号是如果用户是自己是可以的，相当于修改自己的其他信息，手机号不变)
        List<User> userList = userService.selectByPhoneAndId(phoneNumber,loginUserId);
        if(!userList.isEmpty()){
            //3.1.手机号已经注册则报错
            return ResponseResult.error("该手机号已被注册");
        }else{
            //4.满足要求将数据存入数据库中
            updateUserNumber = userService.updateById(user);
            updateAdminNumber = adminService.updateAdmin(admin);
        }
        //5.判断是否修改成功并返回信息
        if(updateAdminNumber==0||updateUserNumber==0){
            return ResponseResult.error("修改管理员信息失败");
        }else{
            return ResponseResult.success("修改管理员信息成功",null);
        }
    }

    @DeleteMapping("/deleteAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult deleteAdmin(@RequestParam Long userId){
        int deleteAdminNumber = 0;//判断删除数量
        //1.根据传入userId判断当前用户状态
        String status = userService.selectUserStatusByUserId(userId);
        if(!status.equals("banned")){
            //1.1不为banned时无法删除，报错
            return ResponseResult.error("无法删除该管理员");
        }else{
            //2.为banned时可以删除，删除数据库中的信息
            //因为admin表关联user表的外键，同时设置了外键约束，所以删除user表中的数据会自动删除admin表中的对应数据
            deleteAdminNumber = userService.deleteUserById(userId);
            //2.1同时还要删除Redis中的数据
            redisCache.deleteObject(LOGIN_USER_KEY+userId);
        }
        //3.根据删除情况返回信息
        if(deleteAdminNumber==0){
            return ResponseResult.error("删除管理员失败");
        }else{
            return ResponseResult.success("删除管理员成功",null);
        }
    }
}

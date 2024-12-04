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
import com.example.familyeducation.vo.AdminVO;
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
    public List<AdminVO> selectAllAdmins() {
        return adminMapper.selectAllAdmins();
    }

    /**
     * @author 小菜
     * @date  2024/11/18
     * @description 插入新管理员信息
     **/
    @Override
    @Transactional
    public int insertAdmin(Admin admin) {
        return adminMapper.insert(admin);
    }


//    /**
//     * @author 小菜
//     * @date  2024/11/18
//     * @description 根据登录管理员id更新管理员信息
//     **/
//    @Override
//    public ResponseResult updateAdmin(User user) {
//
//    }

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

    @Override
    public Long selectAdminId(QueryWrapper<Admin> adminQueryWrapper) {
        Long id = adminMapper.selectOne(adminQueryWrapper).getId();
        return id;
    }

    /**
     * @author 小菜
     * @date  2024/12/4
     * @description 更新admin
     **/
    @Override
    public int updateAdmin(Admin admin) {
        return adminMapper.updateById(admin);
    }

}

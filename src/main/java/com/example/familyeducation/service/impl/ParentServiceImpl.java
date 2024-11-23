package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.ParentDTO;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.Parent;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.ParentMapper;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.ParentService;
import com.example.familyeducation.vo.ParentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/19 14:43
 **/
@Service
public class ParentServiceImpl implements ParentService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ParentMapper parentMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult selectAllParents() {
        //1.查询user表中的数据
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role","parent");
        List<User> parents = userMapper.selectList(queryWrapper);
        //2.将user数据封装为ParentVO
        List<ParentVO> parentVOS = parents.stream().map(user -> {
            ParentVO parentVO = new ParentVO();
            parentVO.setUsername(user.getUsername());
            parentVO.setPhoneNumber(user.getPhoneNumber());
            parentVO.setRole(user.getRole());
            parentVO.setStatus(user.getStatus());
            parentVO.setEmail(user.getEmail());
            parentVO.setPicture(user.getPicture());
            return parentVO;
        }).collect(Collectors.toList());
        return  ResponseResult.success("查询成功",parentVOS);
    }

    @Override
    public ResponseResult updateParent(ParentDTO parentDTO) {
        int updateParentNumber = 0;//插入数据条数
        //1.先从Holder中获取当前登录的家长id
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        //2.将parentDTO封装为user和parent
        User user = new User();
        BeanUtils.copyProperties(parentDTO,user);
        user.setId(loginUserId);

        Parent parent = new Parent();
        parent.setUserId(loginUserId);
        BeanUtils.copyProperties(parentDTO,parent);
        //通过user_id找parent表中的主键id
        QueryWrapper<Parent> parentQueryWrapper = new QueryWrapper<Parent>().eq("user_id", loginUserId);
        Long parentId = parentMapper.selectOne(parentQueryWrapper).getId();
        parent.setId(parentId);
        //密码加密
        String rawPassword = parentDTO.getPassword();
        String password = passwordEncoder.encode(rawPassword);
        user.setPassword(password);
        //3.判断user中的数据是否非空
        if(parentDTO.getUsername()==null||parentDTO.getPhoneNumber()==null||password==null||parentDTO.getRole()==null||
                StringUtils.isEmpty(parentDTO.getUsername()) ||StringUtils.isEmpty(parentDTO.getPhoneNumber())||StringUtils.isEmpty(password)||StringUtils.isEmpty(parentDTO.getRole())){
            //3.1数据不完整报错
            return ResponseResult.error("数据填写不完整，修改管理员信息失败");
        }else{
            //4.数据完整检查手机号是否唯一
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone_number",parentDTO.getPhoneNumber()).ne("id",loginUserId);
            List<User> userList = userMapper.selectList(queryWrapper);
            if(!userList.isEmpty()){
                //4.1手机号不唯一，报错
                return ResponseResult.error("该手机号已被注册");
            }else{
                //5.不存在相同手机号则将数据分别插入user表和teacher表中
                updateParentNumber = userMapper.updateById(user);
                parentMapper.updateById(parent);
            }
        }
        if(updateParentNumber==0){
            return ResponseResult.error("家长信息更新失败");
        }else{
            return ResponseResult.success("家长信息更新成功",null);
        }
    }

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 根据条件查询parent_id
     **/
    @Override
    public Long selectParentId(QueryWrapper<Parent> parentQueryWrapper) {
        Long id = parentMapper.selectOne(parentQueryWrapper).getId();
        return id;
    }
}

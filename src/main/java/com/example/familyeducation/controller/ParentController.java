package com.example.familyeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.ParentDTO;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.Parent;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.ParentService;
import com.example.familyeducation.service.UserService;
import com.example.familyeducation.utils.RedisCache;
import com.example.familyeducation.vo.ParentVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.familyeducation.constants.RedisConstants.LOGIN_USER_KEY;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/19 14:41
 **/

@RestController
@RequestMapping("/parent")
@Tag(name="家长接口",description = "管理家长信息")
public class ParentController {
    @Autowired
    private ParentService parentService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisCache redisCache;

    //TODO这里只有管理员能查询家长信息，现在这里为了方便测试家长也能查询
    @GetMapping("/selectAllParents")
    @PreAuthorize("hasAnyRole('ADMIN','PARENT')")
    public ResponseResult selectAllParents(){
        List<ParentVO> parentVOList = parentService.selectAllParents();
        if(parentVOList.isEmpty()){
            return ResponseResult.success("查询数据为空",null);
        }else{
            return ResponseResult.success("查询数据成功",parentVOList);
        }
    }

    @PutMapping("/updateParent")
    @PreAuthorize("hasRole('PARENT')")
    @Transactional
    public ResponseResult updateParent(@RequestBody ParentDTO parentDTO){
        int updateParentNumber = 0;//插入数据条数
        int updateUserNumber = 0;
        //1.将parentDTO封装为user和parent
        User user = new User();
        BeanUtils.copyProperties(parentDTO,user);

        Parent parent = new Parent();
        parent.setUserId(parentDTO.getId());
        parent.setId(parentDTO.getParentId());
        parent.setRealName(parentDTO.getRealName());
        //2.判断修改的是否是自己的信息
        //2.1不是报错
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        if(loginUserId!= parentDTO.getId()){
            return ResponseResult.error("无法修改别人的信息");
        }
        //密码加密
        String rawPassword = parentDTO.getPassword();
        String password = passwordEncoder.encode(rawPassword);
        user.setPassword(password);
        //3.判断user中的数据是否非空
        if(parentDTO.getUsername()==null||parentDTO.getPhoneNumber()==null||password==null||
                StringUtils.isEmpty(parentDTO.getUsername()) ||StringUtils.isEmpty(parentDTO.getPhoneNumber())||StringUtils.isEmpty(password)){
            //3.1数据不完整报错
            return ResponseResult.error("数据填写不完整，修改家长信息失败");
        }else{
            //4.数据完整检查手机号是否唯一
            List<User> userList = userService.selectByPhoneAndId(parentDTO.getPhoneNumber(),parentDTO.getId());
            if(!userList.isEmpty()){
                //4.1手机号不唯一，报错
                return ResponseResult.error("该手机号已被注册");
            }else{
                //5.不存在相同手机号则将数据分别插入user表和teacher表中
               updateUserNumber = userService.updateById(user);
               updateParentNumber = parentService.updateParent(parent);
            }
        }
        if(updateParentNumber==0||updateUserNumber==0){
            return ResponseResult.error("家长信息更新失败");
        }else{
            return ResponseResult.success("家长信息更新成功",null);
        }
    }

    @DeleteMapping("/deleteParent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult deleteTeacher(@RequestParam Long userId){
        int deleteAdminNumber = 0;//判断删除数量
        //1.根据传入userId判断当前用户状态
        String status = userService.selectUserStatusByUserId(userId);
        if(!status.equals("banned")){
            //1.1不为banned时无法删除，报错
            return ResponseResult.error("无法删除该家长");
        }else{
            //2.为banned时可以删除，删除数据库中的信息
            //因为admin表关联user表的外键，同时设置了外键约束，所以删除user表中的数据会自动删除admin表中的对应数据
            deleteAdminNumber = userService.deleteUserById(userId);
            //2.1同时还要删除Redis中的数据
            redisCache.deleteObject(LOGIN_USER_KEY+userId);
        }
        //3.根据删除情况返回信息
        if(deleteAdminNumber==0){
            return ResponseResult.error("删除家长失败");
        }else{
            return ResponseResult.success("删除家长成功",null);
        }
    }

    @PutMapping("/updateParentStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult updateTeacherStatus(@RequestParam Long userId){
        int updateUserNumber = 0;//插入数据条数
        //1.封装user对象
        User user = new User();
        user.setId(userId);
        String status = userService.selectUserStatusByUserId(userId);
        //修改教师状态
        if(status.equals("banned")){
            user.setStatus("active");
        }
        if(status.equals("active")){
            user.setStatus("banned");
        }
        updateUserNumber = userService.updateById(user);
        if(updateUserNumber==0){
            return ResponseResult.error("状态更新失败");
        }else{
            return ResponseResult.success("状态更新成功",null);
        }
    }
}

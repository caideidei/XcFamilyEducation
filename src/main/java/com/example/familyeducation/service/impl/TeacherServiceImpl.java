package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.TeacherDTO;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.TeacherMapper;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.TeacherService;
import com.example.familyeducation.vo.TeacherVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/18 20:06
 **/
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * @author 小菜
     * @date  2024/11/18
     * @description 查询所有教师的部分信息（从user和teacher中）
     **/
    @Override
    public List<TeacherVO> selectAllTeachers() {
        //1.查询两张表中的数据并封装为TeacherVO
        List<TeacherVO> teacherVOS = teacherMapper.selectAllTeachers();
        //2.返回数据
        return teacherVOS;
    }

    /**
     * @author 小菜
     * @date  2024/11/19
     * @description 修改教师状态
     **/
    @Override
    public ResponseResult updateTeacherStatus(TeacherDTO teacherDTO) {
        int updateTeacherNumber = 0;//插入数据条数
        //1.将teacherDTO封装为user
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        //因为管理员只能修改status，所以这里可以根据phone_number来查询到教师id
        userQueryWrapper.eq("phone_number",teacherDTO.getPhoneNumber());
        User updatedTeacher = userMapper.selectOne(userQueryWrapper);
        Long teacherId = updatedTeacher.getId();
        User user = new User();
        BeanUtils.copyProperties(teacherDTO,user);
        user.setId(teacherId);
        String status = updatedTeacher.getStatus();
        //修改教师状态
        if(status.equals("banned")){
            user.setStatus("active");
        }
        if(status.equals("active")){
        user.setStatus("banned");
        }
        //密码加密
        String rawPassword = teacherDTO.getPassword();
        String password = passwordEncoder.encode(rawPassword);
        user.setPassword(password);
        //3.判断user中的数据是否非空
        if(teacherDTO.getUsername()==null||teacherDTO.getPhoneNumber()==null||password==null||teacherDTO.getRole()==null||
                StringUtils.isEmpty(teacherDTO.getUsername()) ||StringUtils.isEmpty(teacherDTO.getPhoneNumber())||StringUtils.isEmpty(password)||StringUtils.isEmpty(teacherDTO.getRole())){
            //3.1数据不完整报错
            return ResponseResult.error("数据填写不完整，修改教师状态失败");
        }else{
            //4.数据完整检查手机号是否唯一
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone_number",teacherDTO.getPhoneNumber()).ne("id",teacherId);
            List<User> userList = userMapper.selectList(queryWrapper);
            if(!userList.isEmpty()){
                //4.1手机号不唯一，报错
                return ResponseResult.error("该手机号已被注册");
            }else{
                //5.不存在相同手机号则将数据分别插入user表和teacher表中
                updateTeacherNumber = userMapper.updateById(user);
            }
        }
        if(updateTeacherNumber==0){
            return ResponseResult.error("教师状态更新失败");
        }else{
            return ResponseResult.success("教师状态更新成功",null);
        }
    }

    @Override
    public Long selectTeacherId(QueryWrapper<Teacher> teacherQueryWrapper) {
        return teacherMapper.selectOne(teacherQueryWrapper).getId();
    }

    @Override
    public Teacher selectById(QueryWrapper<Teacher> teacherQueryWrapper) {
        return teacherMapper.selectOne(teacherQueryWrapper);
    }

    /**
     * @author 小菜
     * @date  2024/12/4
     * @description 修改teacher表的信息
     **/
    @Override
    public int update(Teacher teacher) {
        return teacherMapper.updateById(teacher);
    }

}

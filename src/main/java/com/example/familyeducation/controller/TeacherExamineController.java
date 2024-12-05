package com.example.familyeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.TeacherExamineDTO;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.entity.TeacherExamine;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.TeacherExamineService;
import com.example.familyeducation.service.TeacherService;
import com.example.familyeducation.utils.GetUserIdUtil;
import com.example.familyeducation.utils.ValidationUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.familyeducation.constants.TeacherExamineConstants.*;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/24 14:52
 **/
@RestController
@RequestMapping("/examine")
@Tag(name="教师审核接口",description = "管理教师审核信息")
public class TeacherExamineController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TeacherExamineService teacherExamineService;

    @Autowired
    private GetUserIdUtil getUserIdUtil;

    /**
     * @author 小菜
     * @date  2024/11/24
     * @description 新增审核信息
     **/
    @PostMapping("/insertExamine")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseResult insertExamine(@RequestBody TeacherExamineDTO teacherExamineDTO){
        //1.判断数据是否非空
        if(!ValidationUtil.areAllFieldsNonNull(teacherExamineDTO)){
            //1.1数据不完整，报错
            return ResponseResult.error("数据填写不完整");
        }
        //2.数据完整，封装数据teacherId，status，createdAt
        TeacherExamine teacherExamine = new TeacherExamine();
        BeanUtils.copyProperties(teacherExamineDTO,teacherExamine);
        teacherExamine.setTeacherId(getUserIdUtil.getTeacherId());
        teacherExamine.setStatus(EXAMINE_PENDING);
        teacherExamine.setCreatedAt(LocalDateTime.now());
        //3.插入数据库
        int insertExamineNumber = teacherExamineService.insert(teacherExamine);
        //4.判断插入情况并返回
        if(insertExamineNumber==0){
            return ResponseResult.error("新增审核信息失败");
        }else{
            return ResponseResult.success("新增审核信息成功",null);
        }
    }

    /**
     * @author 小菜
     * @date  2024/11/24
     * @description 管理员通过审核
     **/
    @PutMapping("/approvedOrRejectedExamine")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseResult approvedExamine(@RequestBody TeacherExamine teacherExamine){
        String status = teacherExamine.getStatus();
        //审核通过
        if(status.equals(EXAMINE_APPROVED)) {
            if(teacherExamine.getReason().isEmpty()){
                teacherExamine.setReason(EXAMINE_APPROVED_REASON);
            }
            //1.封装数据admin_id,status,reason
            teacherExamine.setAdminId(getUserIdUtil.getAdminId());
            teacherExamine.setStatus(EXAMINE_APPROVED);
            teacherExamine.setCreatedAt(LocalDateTime.now());
            //2.将数据存到审核表和教师信息表
            int updateTeacherExamineNumber = teacherExamineService.update(teacherExamine);
            Long teacherId = teacherExamine.getTeacherId();
            QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
            teacherQueryWrapper.eq("id", teacherId);
            Teacher teacher = teacherService.selectById(teacherQueryWrapper);
//        BeanUtils.copyProperties(teacherExamine,teacher);//这里不能直接复制，因为会修改teacher表中的id

            teacher.setRealName(teacherExamine.getRealName());
            teacher.setQualification(teacherExamine.getQualification());
            teacher.setIntro(teacherExamine.getIntro());
            teacher.setOfficialTeacher(1);
            teacher.setSubjects(teacherExamine.getSubjects());

            int updateTeacherNumber = teacherService.update(teacher);
            //3.根据数据更新情况返回信息给前端
            if (updateTeacherExamineNumber == 0 || updateTeacherNumber == 0) {
                return ResponseResult.error("审核信息更新失败");
            } else {
                return ResponseResult.success("审核信息更新成功", null);
            }
        }else {
            //审核失败
            //1.封装数据admin_id,status,reason
            if(teacherExamine.getReason().isEmpty()){
                teacherExamine.setReason(EXAMINE_REJECTED_REASON);
            }
            teacherExamine.setAdminId(getUserIdUtil.getAdminId());
            teacherExamine.setStatus(EXAMINE_REJECTED);
            teacherExamine.setCreatedAt(LocalDateTime.now());
            //2.将数据存到审核表和教师信息表
            int updateTeacherExamineNumber = teacherExamineService.update(teacherExamine);

            //3.根据数据更新情况返回信息给前端
            if(updateTeacherExamineNumber==0){
                return ResponseResult.error("审核信息更新失败");
            }else{
                return ResponseResult.success("审核信息更新成功",null);
            }
        }
    }

//    /**
//     * @author 小菜
//     * @date  2024/11/24
//     * @description 管理员不通过审核
//     **/
//    @PutMapping("/rejectedExamine")
//    @PreAuthorize("hasRole('ADMIN')")
//    @Transactional
//    public ResponseResult rejectedExamine(@RequestBody TeacherExamine teacherExamine){
//
//    }

    /**
     * @author 小菜
     * @date  2024/11/24
     * @description 更新审核信息
     **/
    @PutMapping("/updateExamine")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseResult updateExamine(@RequestBody TeacherExamine teacherExamine){
        //TODO 是否要判断该审核信息是否是自己的？？？
        //1.判断新的审核信息是否完整
        TeacherExamineDTO teacherExamineDTO = new TeacherExamineDTO();
        BeanUtils.copyProperties(teacherExamine,teacherExamineDTO);
        if(!ValidationUtil.areAllFieldsNonNull(teacherExamineDTO)){
            //1.1审核不完整，报错
            return ResponseResult.error("审核信息填写不完整");
        }
        //2.审核完整，更新对象中的对应数据
        teacherExamine.setAdminId(null);
        teacherExamine.setStatus(EXAMINE_PENDING);
        teacherExamine.setReason(null);
        //3.将数据插入数据库
        int updateTeacherExamineNumber  = teacherExamineService.update(teacherExamine);
        //4.根据数据更新情况返回信息给前端
        if(updateTeacherExamineNumber==0){
            return ResponseResult.error("数据更新失败");
        }else{
            return ResponseResult.success("数据更新成功",null);
        }
    }

    @GetMapping("/selectMyExamines")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseResult selectMyExamines(){
        //1.获取当前登录教师id
        Long teacherId = getUserIdUtil.getTeacherId();
        List<TeacherExamine> teacherExamineList = teacherExamineService.selectById(teacherId);
        return ResponseResult.success("成功查询审核信息:",teacherExamineList);
    }

    @GetMapping("/selectAllExamines")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult selectAllExamines(){
        List<TeacherExamine> teacherExamineList = teacherExamineService.selectAll();
        if(teacherExamineList.isEmpty()){
            return ResponseResult.success("查询数据为空",null);
        }else{
            return ResponseResult.success("查询成功",teacherExamineList);
        }
    }

}

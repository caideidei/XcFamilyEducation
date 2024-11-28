package com.example.familyeducation.controller;

import com.example.familyeducation.dto.TeacherDTO;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.TeacherService;
import com.example.familyeducation.vo.AdminVO;
import com.example.familyeducation.vo.TeacherVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/18 20:04
 **/
@RestController
@RequestMapping("/teacher")
@Tag(name="教师接口",description = "管理教师相关信息")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/selectAllTeachers")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseResult selectAllTeachers(){
        List<TeacherVO> teacherVOList = teacherService.selectAllTeachers();
        if(teacherVOList.isEmpty()){
            return ResponseResult.success("查询数据为空",null);
        }else{
            return ResponseResult.success("查询数据成功",teacherVOList);
        }
    }

    @PutMapping("/updateTeacher")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseResult updateTeacher(@RequestBody TeacherDTO teacherDTO){
        return teacherService.updateTeacher(teacherDTO);
    }

    @PutMapping("/updateTeacherStatus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult updateTeacherStatus(@RequestBody TeacherDTO teacherDTO){
        return teacherService.updateTeacherStatus(teacherDTO);
    }
}

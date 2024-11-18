package com.example.familyeducation.controller;

import com.example.familyeducation.dto.TeacherDTO;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/18 20:04
 **/
@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping("/selectAllTeachers")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseResult selectAllTeachers(){
        return teacherService.selectAllTeachers();
    }

    @PostMapping("/updateTeacher")
    @PreAuthorize("hasAnyRole('TEACHER')")
    public ResponseResult updateTeacher(@RequestBody TeacherDTO teacherDTO){
        return teacherService.updateTeacher(teacherDTO);
    }

    @PostMapping("/updateTeacherStatus")
    public ResponseResult updateTeacherStatus(@RequestBody TeacherDTO teacherDTO){
        return teacherService.updateTeacherStatus(teacherDTO);
    }
}

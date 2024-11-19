package com.example.familyeducation.controller;

import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/19 14:41
 **/

@RestController
@RequestMapping("/parent")
public class ParentController {
    @Autowired
    private ParentService parentService;

    @GetMapping("/selectAllParents")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult selectAllParents(){
        return parentService.selectAllParents();
    }
}

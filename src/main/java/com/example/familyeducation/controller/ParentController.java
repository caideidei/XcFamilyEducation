package com.example.familyeducation.controller;

import com.example.familyeducation.dto.ParentDTO;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.ParentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/selectAllParents")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult selectAllParents(){
        return parentService.selectAllParents();
    }

    @PutMapping("/updateParent")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult updateParent(@RequestBody ParentDTO parentDTO){
        return parentService.updateParent(parentDTO);
    }
}

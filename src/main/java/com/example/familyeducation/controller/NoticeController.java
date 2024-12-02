package com.example.familyeducation.controller;

import com.example.familyeducation.dto.NoticeDTO;
import com.example.familyeducation.entity.Notice;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.NoticeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/12 8:30
 **/
@RestController
@RequestMapping("/notice")
@Tag(name="公告接口",description = "管理公告信息")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/selectAllNotices")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseResult selectAllAnnouncements(){
        List<Notice> noticeList = noticeService.selectAllNotices();
        if(noticeList.isEmpty()){
            return ResponseResult.success("查询数据为空",null);
        }else{
            return ResponseResult.success("查询成功",noticeList);
        }
    }


    @PostMapping("/insertNotice")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult insertNotice(@RequestBody NoticeDTO noticeDTO){
        return noticeService.insertNotice(noticeDTO);
    }

    @DeleteMapping("/deleteNotice")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult deleteNotice(@RequestBody Notice notice){
        return noticeService.deleteNotice(notice);
    }

}

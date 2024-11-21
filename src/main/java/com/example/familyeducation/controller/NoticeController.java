package com.example.familyeducation.controller;

import com.example.familyeducation.dto.NoticeDTO;
import com.example.familyeducation.entity.Notice;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/12 8:30
 **/
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/selectAllNotices")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseResult selectAllAnnouncements(){
        return noticeService.selectAllNotices();
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

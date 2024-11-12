package com.example.familyeducation.controller;

import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseResult selectAllAnnouncements(){
        return noticeService.selectAllNotices();
    }

}

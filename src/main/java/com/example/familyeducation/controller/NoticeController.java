package com.example.familyeducation.controller;

import com.example.familyeducation.dto.NoticeDTO;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.Notice;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.NoticeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Repeatable;
import java.time.LocalDateTime;
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
    public ResponseResult selectAllNotices(){
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

        int insertNoticeNumber = 0;
        //1.先封装noticeDTO数据为notice对象
        Notice notice = new Notice();
        BeanUtils.copyProperties(noticeDTO,notice);
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        notice.setCreatedBy(loginUserId);
        notice.setCreatedAt(LocalDateTime.now());
        notice.setExpirationDate(null);
        //2.判断非空
        if(notice.getTitle()==null||notice.getContent()==null
                || StringUtils.isEmpty(notice.getTitle())||StringUtils.isEmpty(notice.getContent())){
            return ResponseResult.error("传入数据不完整，新增失败");
        }else{
            //3.将数据传入数据库
            insertNoticeNumber = noticeService.insertNotice(notice);
        }
        //4.判断并返回前端
        if(insertNoticeNumber==0){
            return ResponseResult.error("新增公告失败");
        }else{
            return ResponseResult.success("新增公告成功",null);
        }

    }

    @DeleteMapping("/deleteNotice")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult deleteNotice(@RequestParam Long id){
        //直接删除即可
        int deleteNoticeNumber = noticeService.deleteNotice(id);
        if(deleteNoticeNumber==0){
            return ResponseResult.error("删除失败");
        }else{
            return ResponseResult.success("删除成功",null);
        }
    }

    @PutMapping("/updateNotice")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult updateNotice(@RequestBody Notice notice){
        //1.判断必要数据是否非空
        //1.1数据不完整则报错
        if(notice.getTitle()==null||notice.getContent()==null
                ||!StringUtils.hasLength(notice.getTitle())||!StringUtils.hasLength(notice.getContent())){
            return ResponseResult.error("数据填写不完整");
        }
        //1.2将公告发布者变为当前登录用户
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        notice.setCreatedBy(loginUserId);
        notice.setCreatedAt(LocalDateTime.now());
        //2.数据完整则进行修改
        int updateNoticeNumber = noticeService.updateNotice(notice);
        if(updateNoticeNumber==0){
            return ResponseResult.error("数据更新失败");
        }else{
            return ResponseResult.success("数据更新成功",null);
        }
    }

}

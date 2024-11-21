package com.example.familyeducation.service.impl;

import com.example.familyeducation.dto.NoticeDTO;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.Notice;
import com.example.familyeducation.mapper.NoticeMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.NoticeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/12 8:30
 **/
@Service
public class NoticeServiceImpl implements NoticeService {

    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public ResponseResult selectAllNotices() {
        List<Notice> mapperList = noticeMapper.selectList(null);
        return ResponseResult.success("查询成功",mapperList);
    }

    @Override
    public ResponseResult insertNotice(NoticeDTO noticeDTO) {
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
            insertNoticeNumber = noticeMapper.insert(notice);
        }
        //4.判断并返回前端
        if(insertNoticeNumber==0){
            return ResponseResult.error("新增公告失败");
        }else{
            return ResponseResult.success("新增公告成功",null);
        }

    }

    @Override
    public ResponseResult deleteNotice(Notice notice) {
        //直接删除即可
        int deleteNoticeNumber = noticeMapper.deleteById(notice);
        if(deleteNoticeNumber==0){
            return ResponseResult.error("删除失败");
        }else{
            return ResponseResult.success("删除成功",null);
        }
    }


}

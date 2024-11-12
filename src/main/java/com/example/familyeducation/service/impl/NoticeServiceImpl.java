package com.example.familyeducation.service.impl;

import com.example.familyeducation.entity.Notice;
import com.example.familyeducation.mapper.NoticeMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

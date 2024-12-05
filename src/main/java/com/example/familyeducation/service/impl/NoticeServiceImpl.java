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
    public List<Notice> selectAllNotices() {
        return noticeMapper.selectList(null);
    }

    @Override
    public int insertNotice(Notice notice) {
        return noticeMapper.insert(notice);
    }

    @Override
    public int deleteNotice(Long id) {
        return noticeMapper.deleteById(id);
    }

    @Override
    public int updateNotice(Notice notice) {
        return noticeMapper.updateById(notice);
    }


}

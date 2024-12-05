package com.example.familyeducation.service;

import com.example.familyeducation.dto.NoticeDTO;
import com.example.familyeducation.entity.Notice;
import com.example.familyeducation.response.ResponseResult;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/12 8:29
 **/

public interface NoticeService {
    List<Notice> selectAllNotices();

    int insertNotice(Notice notice);

    int deleteNotice(Long id);

    int updateNotice(Notice notice);
}

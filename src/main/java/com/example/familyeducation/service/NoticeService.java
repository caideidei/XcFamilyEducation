package com.example.familyeducation.service;

import com.example.familyeducation.dto.NoticeDTO;
import com.example.familyeducation.entity.Notice;
import com.example.familyeducation.response.ResponseResult;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/12 8:29
 **/

public interface NoticeService {
    ResponseResult selectAllNotices();

    ResponseResult insertNotice(NoticeDTO noticeDTO);


    ResponseResult deleteNotice(Notice notice);
}

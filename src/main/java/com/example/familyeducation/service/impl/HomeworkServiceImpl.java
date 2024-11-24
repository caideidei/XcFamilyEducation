package com.example.familyeducation.service.impl;

import com.example.familyeducation.entity.Homework;
import com.example.familyeducation.mapper.HomeworkMapper;
import com.example.familyeducation.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/24 10:03
 **/
@Service
public class HomeworkServiceImpl implements HomeworkService {

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Override
    public int insert(Homework homework) {
        int insert = homeworkMapper.insert(homework);
        return insert;
    }
}

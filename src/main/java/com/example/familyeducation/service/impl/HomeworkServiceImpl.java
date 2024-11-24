package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.Homework;
import com.example.familyeducation.mapper.HomeworkMapper;
import com.example.familyeducation.service.HomeworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Homework> selectMyHomeworks(QueryWrapper<Homework> homeworkQueryWrapper) {
        List<Homework> homework = homeworkMapper.selectList(homeworkQueryWrapper);
        return homework;
    }

    @Override
    public int updateHomework(Homework homework) {
        int update = homeworkMapper.updateById(homework);
        return update;
    }
}

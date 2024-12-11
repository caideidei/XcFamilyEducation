package com.example.familyeducation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.Homework;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/24 10:03
 **/
public interface HomeworkService {
    int insert(Homework homework);

    List<Homework> selectHomeworks(QueryWrapper<Homework> homeworkQueryWrapper);

    int updateHomework(Homework homework);

    int deleteHomeworkById(Long id);
}

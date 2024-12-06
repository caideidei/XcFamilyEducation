package com.example.familyeducation.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.Saying;
import com.example.familyeducation.mapper.SayingMapper;
import com.example.familyeducation.service.SayingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/12/6 9:31
 **/
@Service
public class SayingServiceImpl implements SayingService {
    @Autowired
    private SayingMapper sayingMapper;

    @Override
    public String selectById(int value) {
        QueryWrapper<Saying> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",value);
        return sayingMapper.selectOne(queryWrapper).getSaying();
    }
}

package com.example.familyeducation.mapper;

import com.example.familyeducation.entity.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/11 16:53
 **/
@SpringBootTest
public class AdminMapperTest {
    @Autowired
    private AdminMapper adminMapper;
    //增加
    @Test
    public void insertTest(){
        Admin admin = new Admin();
        admin.setId(null);
        admin.setUserId(1);
        adminMapper.insert(admin);
    }
    //查询
    @Test
    public void selectTest(){
        List<Admin> admins = adminMapper.selectList(null);
        admins.forEach(System.out::println);
    }

    @Test
    public void deleteTest(){
        adminMapper.deleteBatchIds(Arrays.asList(1,2,3));
    }

    @Test
    public void updateTest(){
        Admin admin = new Admin();
        admin.setId(5);
        admin.setUserId(2);
        admin.setLastLoginTime(LocalDateTime.now());
        admin.setCreatedAt(LocalDateTime.now());
        adminMapper.updateById(admin);
    }

}

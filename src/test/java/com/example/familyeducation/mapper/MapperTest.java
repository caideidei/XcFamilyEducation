package com.example.familyeducation.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.familyeducation.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
public class MapperTest {

    @Autowired
    private UserMapper userMapper;

    //测试mybatis-plus查询
    @Test
    public void testUserMapper(){
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    //测试插入
    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("小林老师");
        user.setPassword("$2a$10$Z9aDmES148Yl1eVfmyYAXOMCet4Cb4tbdqY5Jix25NCnYsubhY3bO");
        user.setPhoneNumber("222222");
        user.setRole("parent");
        user.setEmail("222222@qq.com");
        int insert = userMapper.insert(user);
        System.out.println("插入成功的数量："+insert);
        System.out.println("插入用户信息："+user);
    }

    //测试更新
    @Test
    public void testUpdateUser(){
        User user = new User();
        user.setUsername("小刘老师");
        user.setPassword("$2a$10$Z9aDmES148Yl1eVfmyYAXOMCet4Cb4tbdqY5Jix25NCnYsubhY3bO");
        user.setPhoneNumber("222222");
        user.setRole("parent");
        user.setEmail("222222@qq.com");
        userMapper.updateById(user);//注意这里传的是实体不是id
    }

    //测试查询单个用户
    @Test
    public void testSelectUserById(){
        User user = userMapper.selectById(1);
        System.out.println(user);
    }

    //测试查询多个用户
    @Test
    public void testSelectUserByIds(){
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 3, 5));
        users.forEach(System.out::println);
    }

    //测试条件查询
    @Test
    public void testSelectByMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("username","小牛老师");
        map.put("id",2);
        List<User> users = userMapper.selectByMap(map);
        users.forEach(System.out::println);
    }

    //测试分页查询
    @Test
    public void testPage(){
        Page<User> page = new Page<>(1, 3);
        userMapper.selectPage(page,null);
        page.getRecords().forEach(System.out::println);
    }

    //测试删除
    @Test
    public void testDelete(){
        userMapper.deleteById(5);
    }

    //条件构造器
    @Test
    public void testWrapper1(){
        //新建条件构造器
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //链式编程:添加查询条件
        //        eq相等   ne不相等，   gt大于，    lt小于         ge大于等于       le 小于等于

        queryWrapper.eq("email","111111@qq.com").ge("id",2);
        userMapper.selectList(queryWrapper).forEach(System.out::println);
        User user = userMapper.selectOne(queryWrapper);//当查询到的数据大于一报错!!!
        System.out.println(user);

        queryWrapper.between("id","2","3");//String不要用between查
        userMapper.selectList(queryWrapper).forEach(System.out::println);
        System.out.print("查询的数量:"+userMapper.selectCount(queryWrapper));
    }

    //模糊查询
    @Test
    public void testWrapper2(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like("username","妈妈");//模糊查询存在"妈妈"
//        queryWrapper.notLike("username","管理员");//模糊查询不存在"管理员"
        queryWrapper.likeRight("username","小");//模糊查询右侧,这里左侧是"小"
        queryWrapper.likeLeft("username","管理员");//模糊查询左侧,这里右侧是"管理员"
        userMapper.selectList(queryWrapper).forEach(System.out::println);
    }



}

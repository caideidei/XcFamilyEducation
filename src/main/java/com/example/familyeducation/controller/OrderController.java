package com.example.familyeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.OrderDTO;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.Order;
import com.example.familyeducation.entity.Parent;
import com.example.familyeducation.entity.User;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.OrderService;
import com.example.familyeducation.service.ParentService;
import com.example.familyeducation.service.impl.UserDetailsServiceImpl;
import com.example.familyeducation.utils.ValidationUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/23 8:49
 **/
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ParentService parentService;

    @PostMapping("/insertOrder")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult insertOrder(@RequestBody OrderDTO orderDTO){
        int insertOrderNumber = 0;
        //1.先判断数据非空
        boolean allFieldsNonNull = ValidationUtil.areAllFieldsNonNull(orderDTO);
        if(allFieldsNonNull==false){
            //1.1数据不完整，返回错误信息给前端
            return ResponseResult.error("数据不完整，请重新填写");
        }else{
            //2.数据完整
            //2.1获取orderDTO中的数据并将数据封装为order对象
            Order order = new Order();
            BeanUtils.copyProperties(orderDTO,order);
            //2.2获取当前登录用户id对应的parent_id并将其赋值给parent_id
            LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Long loginUserId = loginUser.getUser().getId();//loginUserId对应的就是user_id

            QueryWrapper<Parent> parentQueryWrapper = new QueryWrapper<>();
            parentQueryWrapper.eq("user_id",loginUserId);
            Long parentId = parentService.selectParentId(parentQueryWrapper);
            order.setParentId(parentId);
            //3.插入数据到数据库
            insertOrderNumber = orderService.insertOrder(order);
        }
        //4.判断插入情况
        if(insertOrderNumber==0){
            return ResponseResult.error("新增家教订单失败");
        }else{
            return ResponseResult.success("成功新增家教订单",null);
        }
    }
}

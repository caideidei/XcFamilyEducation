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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.familyeducation.constants.OrderConstants.ORDER_REVIEW_FAILED;
import static com.example.familyeducation.constants.OrderConstants.ORDER_REVIEW_PASSED;

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

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 家长发布订单
     **/
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

            order.setCreatedAt(LocalDateTime.now());
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

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 管理员审核订单状态为通过
     **/
    @PutMapping("/passOrder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult passOrder(@RequestBody Order order){
        //1.直接修改状态为通过并传给数据库
        order.setStatus(ORDER_REVIEW_PASSED);
        int update = 0;
        update = orderService.updateStatusToPassed(order);
        if(update==0){
            return ResponseResult.error("修改状态失败");
        }else {
            return ResponseResult.success("修改状态成功",null);
        }
    }

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 管理员审核订单为失败状态
     **/
    @PutMapping("/failOrder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult failOrder(@RequestBody Order order){
        //1.直接修改状态为通过并传给数据库
        order.setStatus(ORDER_REVIEW_FAILED);
        int update = 0;
        update = orderService.updateStatusToPassed(order);
        if(update==0){
            return ResponseResult.error("修改状态失败");
        }else {
            return ResponseResult.success("修改状态成功",null);
        }
    }

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 查询自己的订单信息
     **/
    @GetMapping("/selectMyOrders")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult selectMyOrders(){
        //1.获取当前登录用户id并查询对应parent_id
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        QueryWrapper<Parent> parentQueryWrapper = new QueryWrapper<>();
        parentQueryWrapper.eq("user_id",loginUserId);
        Long parentId = parentService.selectParentId(parentQueryWrapper);
        //2.根据当前parentId查询对应订单数据
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("parent_id",parentId);
        List<Order> orderList = orderService.selectMyOrders(orderQueryWrapper);
        return ResponseResult.success("成功查询到订单",orderList);
    }


}

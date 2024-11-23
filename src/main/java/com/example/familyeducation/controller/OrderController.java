package com.example.familyeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.OrderDTO;
import com.example.familyeducation.entity.*;
import com.example.familyeducation.mapper.UserMapper;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.OrderService;
import com.example.familyeducation.service.ParentService;
import com.example.familyeducation.service.TeacherService;
import com.example.familyeducation.service.impl.UserDetailsServiceImpl;
import com.example.familyeducation.utils.RedisCache;
import com.example.familyeducation.utils.ValidationUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.example.familyeducation.constants.OrderConstants.*;
import static com.example.familyeducation.constants.RedisConstants.ORDER_MESSAGE_KEY;
import static com.example.familyeducation.constants.RedisConstants.ORDER_MESSAGE_TTL;

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

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private RedisCache redisCache;

    //TODO 修改数据库的数据时要修改Redis中的对应数据，同时查询所有订单时，如果其他有订单改变，就不会查询完整（Redis数据更新问题）

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
            Long parentId = getParentId();
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
        update = orderService.updateOrder(order);
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
        update = orderService.updateOrder(order);
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
        Long parentId = getParentId();
        //2.根据当前parentId查询对应订单数据
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("parent_id",parentId);
        List<Order> orderList = orderService.selectMyOrders(orderQueryWrapper);
        return ResponseResult.success("成功查询到订单",orderList);
    }

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 重新修改订单（审核失败，成功的订单）
     **/
    @PutMapping("/republishOrder")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult republishOrder(@RequestBody Order order){
        int updateOrderNumber = 0;
        //1.获取当前登录用户id
        Long parentId = getParentId();
        //2.判断修改的是否是自己的订单
        if(parentId!=order.getParentId()){
            //2.1修改的不是自己的订单，报错
            return ResponseResult.error("无法修改别人的订单");
        }else{
            //3.修改的是自己的订单
            //3.1修改新订单状态
            order.setStatus(ORDER_PENDING_REVIEW);
            //3.2将新订单对象插入数据库
            updateOrderNumber = orderService.updateOrder(order);
        }
        //4.根据插入情况向前端返回数据
        if(updateOrderNumber==0){
            return ResponseResult.error("修改订单失败");
        }else{
            return ResponseResult.success("修改订单成功",null);
        }
    }

    @GetMapping("/selectAllOrders")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseResult selectAllOrders(){
        List<Order> orderList;
        //1.从Redis中查询数据,查询以xc:order:开头的所有键
        Collection<String> keys = redisCache.keys(ORDER_MESSAGE_KEY+"*");

        if(keys!=null&&!keys.isEmpty()){
            //1.1Redis中存在对应数据直接返回,可以取得每个订单并添加到List中返回，也可以使用下面方法
            orderList= keys.stream()
                    .map(key->(Order)redisCache.getCacheObject(key))
                    .collect(Collectors.toList());
            if(!orderList.isEmpty()){
                return ResponseResult.success("成功查询订单(Redis):",orderList);
            }
        }
        //2.Redis中不存在对应数据则从数据库中查询数据
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.notIn("status", Arrays.asList(ORDER_PENDING_REVIEW,ORDER_REVIEW_FAILED));
        orderList= orderService.selectAllOrders(orderQueryWrapper);
        //2.1数据库中不存在对应数据，返回错误信息
        if(orderList==null||orderList.isEmpty()){
            return ResponseResult.error("没有订单信息");
        }
        //3.数据库存在数据，将数据存到Redis中
        for (Order order : orderList) {
            String key = ORDER_MESSAGE_KEY + order.getId();
            redisCache.setCacheObject(key,order,ORDER_MESSAGE_TTL, TimeUnit.MINUTES);
        }
        //3.1将从数据库中查询到的数据返回前端
        return ResponseResult.success("成功查询订单(数据库):",orderList);
    }

    @PutMapping("/inTrial")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseResult inTrail(@RequestBody Order order){
        //1.判断订单状态，是否能进行试课
        if(!order.getStatus().equals(ORDER_REVIEW_PASSED)){
            //1.1订单不为reviewPassed不能进行试课
            return ResponseResult.error("无法修改订单（试课）");
        }
        //2.获得当前登录教师id
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        QueryWrapper<Teacher> teacherQueryWrapper = new QueryWrapper<>();
        teacherQueryWrapper.eq("user_id",loginUserId);
        Long teacherId = teacherService.selectTeacherId(teacherQueryWrapper);
        //3.修改order数据
        order.setStatus(ORDER_IN_TRIAL);
        order.setTeacherId(teacherId);
        //3.存入数据库
        int updateOrder = orderService.updateOrder(order);
        //4.判断更新情况
        if(updateOrder==0){
            return ResponseResult.error("更新订单失败");
        }else{
            return ResponseResult.success("更新订单成功",null);
        }
    }

    @PutMapping("/trialFailed")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult trialFailed(@RequestBody Order order){
        //1.获取当前登录家长id
        Long parentId = getParentId();
        //判断能否进行修改订单
        if(parentId!=order.getParentId()||!order.getStatus().equals(ORDER_IN_TRIAL)){
            return ResponseResult.error("无法修改订单信息");
        }
        //2.修改订单状态
        //mybatis-plus会忽略null值，属性要加上@TableField(updateStrategy = FieldStrategy.IGNORED)注解
        order.setTeacherId(null);
        order.setStatus(ORDER_REVIEW_PASSED);
        //3.更新数据库表
        int updateOrderNumber = orderService.updateOrder(order);
        //4.判断更新情况
        if(updateOrderNumber==0){
            return ResponseResult.error("更新订单信息失败");
        }else{
            return ResponseResult.success("成功更新订单信息",null);
        }
    }

    @PutMapping("/trialPassed")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult trailPassed(@RequestBody Order order){
        //1.获取当前登录家长id
        Long parentId = getParentId();
        //判断能否进行修改订单
        if(parentId!=order.getParentId()||!order.getStatus().equals(ORDER_IN_TRIAL)){
            return ResponseResult.error("无法修改订单信息");
        }
        //2.修改订单状态
        order.setStatus(ORDER_ACCEPTED);
        //3.更新数据库表
        int updateOrderNumber = orderService.updateOrder(order);
        //4.判断更新情况
        if(updateOrderNumber==0){
            return ResponseResult.error("更新订单信息失败");
        }else{
            return ResponseResult.success("成功更新订单信息",null);
        }
    }

    public Long getParentId(){
        LoginUser loginUser = (LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long loginUserId = loginUser.getUser().getId();
        QueryWrapper<Parent> parentQueryWrapper = new QueryWrapper<>();
        parentQueryWrapper.eq("user_id",loginUserId);
        Long parentId = parentService.selectParentId(parentQueryWrapper);
        return parentId;
    }

}

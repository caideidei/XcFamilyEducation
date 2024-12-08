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
import com.example.familyeducation.utils.GetUserIdUtil;
import com.example.familyeducation.utils.RedisCache;
import com.example.familyeducation.utils.ValidationUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
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
@Tag(name="订单接口",description = "管理订单信息")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private GetUserIdUtil getUserIdUtil;

    @Autowired
    private RedisCache redisCache;

    //TODO修改数据库的数据时要修改Redis中的对应数据，同时查询所有订单时，如果其他有订单改变，就不会查询完整（Redis数据更新问题）

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 教师查询自己的订单信息
     **/
    @GetMapping("/teacherSelectOrders")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseResult teacherSelectOrders(){
        //1.获取当前登录用户id并查询对应parent_id
        Long teacherId = getUserIdUtil.getTeacherId();
        //2.根据当前parentId查询对应订单数据
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("teacher_id",teacherId);
        List<Order> orderList = orderService.selectMyOrders(orderQueryWrapper);
        return ResponseResult.success("成功查询到订单",orderList);
    }

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 家长查询自己的订单信息
     **/
    @GetMapping("/parentSelectOrders")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult parentSelectOrders(){
        //1.获取当前登录用户id并查询对应parent_id
        Long parentId = getUserIdUtil.getParentId();
        //2.根据当前parentId查询对应订单数据
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("parent_id",parentId);
        List<Order> orderList = orderService.selectMyOrders(orderQueryWrapper);
        return ResponseResult.success("成功查询到订单",orderList);
    }

    /**
     * @author 小菜
     * @date  2024/12/5
     * @description 查询待接单的订单
     **/
    @GetMapping("/selectAllPassedOrders")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER','PARENT')")
    public ResponseResult selectAllPassedOrders(){

        List<Order> orderList;
        //1.从Redis中查询数据
        Collection<String> keys = redisCache.keys(ORDER_MESSAGE_KEY + "*");
        //2.Redis中为空，从数据库中查询
        if(keys==null||keys.isEmpty()){
            //2.1查询数据库的所有数据
            orderList = orderService.selectAllOrders(new QueryWrapper<Order>().eq("status",ORDER_REVIEW_PASSED));
            //2.2数据库中有数据，将数据存到Redis
            if(orderList!=null&&!orderList.isEmpty()){
                for (Order order : orderList) {
                    String key = ORDER_MESSAGE_KEY+order.getId();
                    redisCache.setCacheObject(key,order,ORDER_MESSAGE_TTL,TimeUnit.MINUTES);
                }
                return ResponseResult.success("成功查询数据",orderList);
            }else{
                //2.3数据库中无数据，报错
                return ResponseResult.error("数据为空");
            }
        }
        //3.Redis中存在数据，判断数据是否完整
        //3.1查询数据库中通过审核的总数
        orderList = orderService.selectAllOrders(new QueryWrapper<Order>().eq("status",ORDER_REVIEW_PASSED));
        //3.2Redis中的数据不完整
        if(orderList.size()!=keys.size()){
            //3.3将数据库中的完整数据更新到Redis中
            if(orderList!=null&&!orderList.isEmpty()){
                for (Order order : orderList) {
                    String key = ORDER_MESSAGE_KEY+order.getId();
                    redisCache.setCacheObject(key,order,ORDER_MESSAGE_TTL,TimeUnit.MINUTES);
                }
                return ResponseResult.success("成功查询数据",orderList);
            }else{
                return ResponseResult.error("数据为空");
            }
        }
        //3，4Redis数据完整，直接返回
        orderList = keys.stream()
                .map(key ->(Order)redisCache.getCacheObject(key))
                .sorted(Comparator.comparing(Order::getId))
                .collect(Collectors.toList());
        return ResponseResult.success("成功查询数据",orderList);
    }

    /**
     * @author 小菜
     * @date  2024/12/5
     * @description 管理员查询所有订单
     **/
    @GetMapping("/selectAllOrders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult selectAllOrders(){
        //1.直接查询所有订单并返回
        List<Order> orderList = orderService.selectAllOrders(null);
        if(orderList==null){
            return ResponseResult.error("查询数据为空");
        }else{
            return ResponseResult.success("查询数据成功",orderList);
        }
    }

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
            Long parentId = getUserIdUtil.getParentId();
            order.setParentId(parentId);

            order.setCreatedAt(LocalDateTime.now());
            //3.插入数据到数据库
            insertOrderNumber = orderService.insertOrder(order);
//            String key = ORDER_MESSAGE_KEY + order.getId();
//            redisCache.setCacheObject(key,order,ORDER_MESSAGE_TTL, TimeUnit.MINUTES);
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
     * @description 管理员审核订单状态
     **/
    @PutMapping("/passOrFailOrder")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseResult passOrder(@RequestBody Order order){
        //1.直接传给数据库
        int update = 0;
        update = orderService.updateOrder(order);
//        String key = ORDER_MESSAGE_KEY + order.getId();
//        redisCache.setCacheObject(key,order,ORDER_MESSAGE_TTL, TimeUnit.MINUTES);
        if(update==0){
            return ResponseResult.error("修改状态失败");
        }else {
            return ResponseResult.success("修改状态成功",null);
        }
    }

    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 家长重新修改订单（审核失败，成功的订单）
     **/
    @PutMapping("/republishOrder")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult republishOrder(@RequestBody Order order){
        int updateOrderNumber = 0;
        //1.获取当前登录用户id
        Long parentId = getUserIdUtil.getParentId();
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
//            String key = ORDER_MESSAGE_KEY + order.getId();
//            redisCache.setCacheObject(key,order,ORDER_MESSAGE_TTL, TimeUnit.MINUTES);
        }
        //4.根据插入情况向前端返回数据
        if(updateOrderNumber==0){
            return ResponseResult.error("修改订单失败");
        }else{
            return ResponseResult.success("修改订单成功",null);
        }
    }


    /**
     * @author 小菜
     * @date  2024/12/5
     * @description 教师试课
     **/
    @PutMapping("/inTrial")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseResult inTrail(@RequestBody Order order){
        //1.判断订单状态，是否能进行试课
        if(!order.getStatus().equals(ORDER_REVIEW_PASSED)){
            //1.1订单不为reviewPassed不能进行试课
            return ResponseResult.error("无法修改订单（试课）");
        }
        //2.获得当前登录教师id
        Long teacherId = getUserIdUtil.getTeacherId();
        //3.修改order数据
        order.setStatus(ORDER_IN_TRIAL);
        order.setTeacherId(teacherId);
        //3.存入数据库
        int updateOrder = orderService.updateOrder(order);
//        String key = ORDER_MESSAGE_KEY + order.getId();
//        redisCache.setCacheObject(key,order,ORDER_MESSAGE_TTL, TimeUnit.MINUTES);
        //4.判断更新情况
        if(updateOrder==0){
            return ResponseResult.error("更新订单失败");
        }else{
            return ResponseResult.success("更新订单成功",null);
        }
    }

    /**
     * @author 小菜
     * @date  2024/12/5
     * @description 试课失败
     **/
    @PutMapping("/trialFailed")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult trialFailed(@RequestBody Order order){
        //1.获取当前登录家长id
        Long parentId = getUserIdUtil.getParentId();
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
//        String key = ORDER_MESSAGE_KEY + order.getId();
//        redisCache.setCacheObject(key,order,ORDER_MESSAGE_TTL, TimeUnit.MINUTES);
        //4.判断更新情况
        if(updateOrderNumber==0){
            return ResponseResult.error("更新订单信息失败");
        }else{
            return ResponseResult.success("成功更新订单信息",null);
        }
    }

    /**
     * @author 小菜
     * @date  2024/12/5
     * @description 试课通过
     **/
    @PutMapping("/trialPassed")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult trailPassed(@RequestBody Order order){
        //1.获取当前登录家长id
        Long parentId = getUserIdUtil.getParentId();
        //判断能否进行修改订单
        if(parentId!=order.getParentId()||!order.getStatus().equals(ORDER_IN_TRIAL)){
            return ResponseResult.error("无法修改订单信息");
        }
        //2.修改订单状态
        order.setStatus(ORDER_ACCEPTED);
        //3.更新数据库表
        int updateOrderNumber = orderService.updateOrder(order);
//        String key = ORDER_MESSAGE_KEY + order.getId();
//        redisCache.setCacheObject(key,order,ORDER_MESSAGE_TTL, TimeUnit.MINUTES);
        //4.判断更新情况
        if(updateOrderNumber==0){
            return ResponseResult.error("更新订单信息失败");
        }else{
            return ResponseResult.success("成功更新订单信息",null);
        }
    }

}

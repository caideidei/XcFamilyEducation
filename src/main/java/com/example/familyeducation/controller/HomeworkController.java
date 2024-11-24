package com.example.familyeducation.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.PublishHomeworkDTO;
import com.example.familyeducation.entity.Homework;
import com.example.familyeducation.entity.LoginUser;
import com.example.familyeducation.entity.Order;
import com.example.familyeducation.entity.Teacher;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.service.HomeworkService;
import com.example.familyeducation.service.OrderService;
import com.example.familyeducation.service.TeacherService;
import com.example.familyeducation.utils.GetUserIdUtil;
import com.example.familyeducation.utils.ValidationUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.familyeducation.constants.HomeworkConstants.*;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/24 10:04
 **/
@RestController
@RequestMapping("/homework")
public class HomeworkController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private HomeworkService homeworkService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private GetUserIdUtil getUserIdUtil;

    //TODO 作业超时

    @PostMapping("/publish")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseResult publish(@RequestBody PublishHomeworkDTO publishHomeworkDTO){
        //1.判断数据是否非空
        if(!ValidationUtil.areAllFieldsNonNull(publishHomeworkDTO)){
            return ResponseResult.error("作业数据填写不完整");
        }
        //2.根据订单id判断该订单是否是自己的家教订单
        //获取订单中的教师id
        Long orderId = publishHomeworkDTO.getOrderId();
        QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("id",orderId);
        Long teacherId = orderService.getTeacherId(orderQueryWrapper);
        if(teacherId==null){
            return ResponseResult.error("无法找到订单对应教师id");
        }
        //获取登录的教师id
        Long loginTeacherId = getUserIdUtil.getTeacherId();
        //2.1发布的作业不是自己的家教订单，报错
        if(loginTeacherId!=teacherId){
            return ResponseResult.error("发布作业失败");
        }
        //3.发布的是自己的家教订单，将publishHomeworkDTO填充到homework对象中
        Homework homework = new Homework();
        BeanUtils.copyProperties(publishHomeworkDTO,homework);
        //3.1补充homework对象
        homework.setStatus(HOMEWORK_INCOMPLETED);
        homework.setCreatedAt(LocalDateTime.now());
        //4.将数据插入数据库表中
        int insertHomeworkNumber = homeworkService.insert(homework);
        //5.根据插入数据情况返回前端信息
        if(insertHomeworkNumber==0){
            return ResponseResult.error("发布作业失败");
        }else{
            return ResponseResult.success("发布作业成功",null);
        }
    }

    @GetMapping("/selectMyHomeworks")
    @PreAuthorize("hasAnyRole('TEACHER','PARENT')")
    public ResponseResult selectMyHomeworks(@RequestParam Long orderId){
        //之前已经查询到自己的订单了，就不用判断这个作业相关订单是否与自己有关了
        //1.直接返回作业信息
        QueryWrapper<Homework> homeworkQueryWrapper = new QueryWrapper<>();
        homeworkQueryWrapper.eq("order_id",orderId);
        List<Homework> homeworkList = homeworkService.selectMyHomeworks(homeworkQueryWrapper);
        return ResponseResult.success("成功查询作业信息:",homeworkList);
    }

    @PutMapping("/commit")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseResult commitHomework(@RequestBody Homework homework){
        //修改作业状态
        homework.setStatus(HOMEWORK_COMPLETED);
        int updateHomeworkNumber = homeworkService.updateHomework(homework);
        if(updateHomeworkNumber==0){
            return ResponseResult.error("更新作业失败");
        }else{
            return ResponseResult.success("更新作业成功",null);
        }
    }

    @PutMapping("/review")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseResult reviewHomework(@RequestBody Homework homework){
        homework.setStatus(HOMEWORK_EVALUATED);
        int updateHomeworkNumber = homeworkService.updateHomework(homework);
        if(updateHomeworkNumber==0){
            return ResponseResult.error("更新作业失败");
        }else{
            return ResponseResult.success("更新作业成功",null);
        }
    }

}

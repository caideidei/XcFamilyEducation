package com.example.familyeducation.utils;

import com.example.familyeducation.dto.OrderDTO;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/23 9:03
 **/
@SpringBootTest
public class TestUtils {
    /**
     * @author 小菜
     * @date  2024/11/23
     * @description 测试ValidationUtil.areAllFieldsNonNull判断所有数据元素是否非空
     **/
    @Test
    public void validationUtilTest(){
        OrderDTO order = new OrderDTO();
        order.setAddress("1");
        order.setTime("1");
        order.setNote("1");
        order.setGrade("1");
        order.setSubject("1");
        order.setPrice(120L);
        boolean flag1 = ValidationUtil.areAllFieldsNonNull(order);
        System.out.println("当前对象是否全部元素非空:"+flag1);

    }


    @Test
    public void JwtTest() throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJkNjlmOTUyMDVkMGY0ZWY3ODI4MjI4MGEwZjM1ZWYyMSIsInJvbGUiOiJ0ZWFjaGVyIiwic3ViIjoiMTMiLCJpc3MiOiJ4YyIsImlhdCI6MTczMjY3NjE0OCwiZXhwIjoxNzMyNjc5NzQ4fQ.N7rOagExzq871HXTPRptc9vHHR0I5olGCHwEq5thx-0";
        Claims claims = JwtUtil.parseJWT(token);
        System.out.println(claims);
    }

}

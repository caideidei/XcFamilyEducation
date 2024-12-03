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
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMmQ2MTBiNWEzNjY0YzAyYmI4YzhjOTA0MTFhMzU3NiIsInJvbGUiOiJ0ZWFjaGVyIiwic3ViIjoiMTMiLCJpc3MiOiJ4YyIsImlhdCI6MTczMzIxMzI3NiwiZXhwIjoxNzMzMjE2ODc2fQ.GUXvQsxfdi7W4DeExbqPojqzbMxZuP5Hs5LuxllUq7g";
        Claims claims = JwtUtil.parseJWT(token);
//        System.out.println(claims);

        // // 假设你已经得到了JWT字符串token
        //        String token = "your_jwt_token_here";
        //
        //        // 解析Token
        //        Claims claims = JwtUtil.parseJWT(token);
        //
        //        // 获取sub（即subject）
        //        String subject = claims.getSubject();
        //        System.out.println("Subject (sub): " + subject);
        //
        //        // 如果你需要获取其他信息，比如role，可以这么做：
        //        String role = (String) claims.get("role"); // role是你在生成Token时通过claim设置的字段
        //        System.out.println("Role: " + role);
        String subject = claims.getSubject();
        System.out.println(subject);

    }

}

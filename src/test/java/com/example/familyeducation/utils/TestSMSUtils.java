package com.example.familyeducation.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/12/9 19:07
 **/
@SpringBootTest
public class TestSMSUtils {

    @Test
    public void testSendMessage(){
        AliSMSUtils.sendMessage("15750893889","1111");
    }
}

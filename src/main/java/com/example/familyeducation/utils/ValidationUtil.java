package com.example.familyeducation.utils;

import java.lang.reflect.Field;
import org.apache.commons.lang3.StringUtils;

public class ValidationUtil {
    public static boolean areAllFieldsNonNull(Object obj) {
        if (obj == null) {
            return false; // 对象本身为空，直接返回 false
        }

        // 获取对象的所有字段
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // 设置字段为可访问
            try {
                Object value = field.get(obj);
                // 如果字段为空，或者字段是空字符串，直接返回 false
                if (value == null || (value instanceof String && StringUtils.isBlank((String) value))) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("访问字段错误: " + field.getName(), e);
            }
        }
        // 所有字段都非空，返回 true
        return true;
    }
}

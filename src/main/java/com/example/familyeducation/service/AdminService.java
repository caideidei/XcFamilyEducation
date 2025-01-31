package com.example.familyeducation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.entity.Admin;
import com.example.familyeducation.vo.AdminVO;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/11 16:23
 **/
public interface AdminService {
    List<AdminVO> selectAllAdmins();

    int insertAdmin(Admin admin);

    Long selectAdminId(QueryWrapper<Admin> adminQueryWrapper);

    int updateAdmin(Admin admin);

    Admin selectAdminByUserId(Long id);
}

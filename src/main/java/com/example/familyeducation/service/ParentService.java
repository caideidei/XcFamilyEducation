package com.example.familyeducation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.ParentDTO;
import com.example.familyeducation.entity.Parent;
import com.example.familyeducation.response.ResponseResult;
import com.example.familyeducation.vo.ParentVO;

import java.util.List;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/19 14:42
 **/
public interface ParentService {
    List<ParentVO> selectAllParents();

    int updateParent(Parent parent);

    Long selectParentId(QueryWrapper<Parent> parentQueryWrapper);
}

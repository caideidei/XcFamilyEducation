package com.example.familyeducation.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.familyeducation.dto.ParentDTO;
import com.example.familyeducation.entity.Parent;
import com.example.familyeducation.response.ResponseResult;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/19 14:42
 **/
public interface ParentService {
    ResponseResult selectAllParents();

    ResponseResult updateParent(ParentDTO parentDTO);

    Long selectParentId(QueryWrapper<Parent> parentQueryWrapper);
}

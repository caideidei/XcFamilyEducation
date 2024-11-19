package com.example.familyeducation.service;

import com.example.familyeducation.dto.ParentDTO;
import com.example.familyeducation.response.ResponseResult;

/**
 * @ClassDescription:
 * @Author:小菜
 * @Create:2024/11/19 14:42
 **/
public interface ParentService {
    ResponseResult selectAllParents();

    ResponseResult updateParent(ParentDTO parentDTO);
}

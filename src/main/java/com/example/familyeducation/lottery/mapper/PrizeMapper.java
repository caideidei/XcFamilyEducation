package com.example.familyeducation.lottery.mapper;

import com.example.familyeducation.lottery.entity.Prize;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
/**
 * 奖品预热mapper - 查询奖品信息
 */
@Mapper
public interface PrizeMapper {
    @Select("SELECT * FROM prize")
    List<Prize> getAllPrizes();

    Prize selectById(Long prizeId);
}

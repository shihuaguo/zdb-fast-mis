package com.zdb.modules.taxic.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.zdb.modules.taxic.entity.FeedbackInfo;

@Mapper
public interface FeedbackInfoMapper {
    int deleteByPrimaryKey(Integer id);
    
    int deleteBatch(Integer[] ids);

    int insert(FeedbackInfo record);

    int insertSelective(FeedbackInfo record);

    FeedbackInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FeedbackInfo record);

    int updateByPrimaryKey(FeedbackInfo record);
    
    List<FeedbackInfo> queryList(Map<String, Object> map);
    
    int queryTotal(Map<String, Object> map);
}
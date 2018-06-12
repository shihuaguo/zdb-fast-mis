package com.zdb.modules.cal.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.zdb.modules.cal.entity.CalTask;

@Mapper
public interface CalTaskMapper {
    int deleteByPrimaryKey(Integer id);
    
    int deleteBatch(Integer[] ids);

    int insert(CalTask record);

    int insertSelective(CalTask record);

    CalTask selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CalTask record);

    int updateByPrimaryKey(CalTask record);
    
    List<CalTask> queryList(Map<String, Object> map);
    
    int queryTotal(Map<String, Object> map);
}
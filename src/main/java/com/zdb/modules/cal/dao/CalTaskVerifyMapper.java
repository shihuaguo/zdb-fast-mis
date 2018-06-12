package com.zdb.modules.cal.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.zdb.modules.cal.entity.CalTaskVerify;

@Mapper
public interface CalTaskVerifyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CalTaskVerify record);

    int insertSelective(CalTaskVerify record);

    CalTaskVerify selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CalTaskVerify record);

    int updateByPrimaryKey(CalTaskVerify record);
    
    List<CalTaskVerify> queryList(Map<String, Object> map);
    
    List<CalTaskVerify> queryPage(Map<String, Object> map);
    
    int queryTotal(Map<String, Object> map);
}
package com.zdb.modules.cal.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.zdb.modules.cal.entity.CalTaskItem;

@Mapper
public interface CalTaskItemMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CalTaskItem record);

    int insertSelective(CalTaskItem record);

    CalTaskItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CalTaskItem record);

    int updateByPrimaryKey(CalTaskItem record);
    
    //根据任务ID查询事项
    List<CalTaskItem> queryByTaskId(Integer taskId);
    
    //删除任务时,删除对应的事项
    int deleteByTaskId(Integer taskId);
    
    List<CalTaskItem> queryList(Map<String, Object> params);
    
    int queryTotal(Map<String, Object> map);
    
    int moveTaskItem(Map<String, Object> params);
}
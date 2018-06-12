package com.zdb.modules.cal.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.zdb.modules.cal.entity.CalTask;

/**
 * 新建任务
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2017年8月31日
 */
public interface ICalTaskService {
	
	CalTask queryObject(Integer id);
	
	PageInfo<CalTask> queryPage(Map<String, Object> map, int page, int rows);
	
	List<CalTask> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CalTask calTask);
	
	int update(CalTask calTask);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}

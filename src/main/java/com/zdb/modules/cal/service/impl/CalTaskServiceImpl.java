package com.zdb.modules.cal.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zdb.modules.cal.dao.CalTaskMapper;
import com.zdb.modules.cal.entity.CalTask;
import com.zdb.modules.cal.service.ICalTaskService;



@Service("calTaskService")
public class CalTaskServiceImpl implements ICalTaskService {
	@Autowired
	private CalTaskMapper mapper;
	
	@Override
	public CalTask queryObject(Integer id){
		return mapper.selectByPrimaryKey(id);
	}
	
	public PageInfo<CalTask> queryPage(Map<String, Object> map, int page, int rows){
		PageHelper.startPage(page, rows);
		List<CalTask> list = mapper.queryList(map);
		return new PageInfo<>(list);
	}
	
	@Override
	public List<CalTask> queryList(Map<String, Object> map){
		return mapper.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mapper.queryTotal(map);
	}
	
	@Override
	public void save(CalTask calTask){
		mapper.insertSelective(calTask);
	}
	
	@Override
	public int update(CalTask calTask){
		return mapper.updateByPrimaryKeySelective(calTask);
	}
	
	@Override
	public void delete(Integer id){
		mapper.deleteByPrimaryKey(id);
	}
	
	@Override
	public void deleteBatch(Integer[] ids){
		mapper.deleteBatch(ids);
	}
	
}

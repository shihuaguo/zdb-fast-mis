package com.zdb.modules.taxic.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdb.modules.taxic.dao.FeedbackInfoMapper;
import com.zdb.modules.taxic.entity.FeedbackInfo;
import com.zdb.modules.taxic.service.ITaxIcService;

@Service("taxIcService")
public class TaxIcServiceImpl implements ITaxIcService {
	
	@Autowired
	private FeedbackInfoMapper mapper;

	@Override
	public FeedbackInfo queryObject(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	public List<FeedbackInfo> queryList(Map<String, Object> map){
		return mapper.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return mapper.queryTotal(map);
	}

	@Override
	public void save(FeedbackInfo feedbackInfo) {
		mapper.insertSelective(feedbackInfo);
	}

	@Override
	public int update(FeedbackInfo feedbackInfo) {
		return mapper.updateByPrimaryKeySelective(feedbackInfo);
	}

	@Override
	public void delete(Integer id) {
		mapper.deleteByPrimaryKey(id);
	}

	@Override
	public void deleteBatch(Integer[] ids) {
		mapper.deleteBatch(ids);
	}

}

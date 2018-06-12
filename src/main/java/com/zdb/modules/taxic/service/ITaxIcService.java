package com.zdb.modules.taxic.service;

import java.util.List;
import java.util.Map;

import com.zdb.modules.taxic.entity.FeedbackInfo;

/**
 * 与工商税务有关的Service
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2017年8月31日
 */
public interface ITaxIcService {
	
	FeedbackInfo queryObject(Integer id);
	
	List<FeedbackInfo> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(FeedbackInfo feedbackInfo);
	
	int update(FeedbackInfo feedbackInfo);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}

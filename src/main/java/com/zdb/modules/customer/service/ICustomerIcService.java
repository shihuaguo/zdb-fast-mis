package com.zdb.modules.customer.service;

import com.zdb.modules.customer.entity.CustomerIndustryCommerce;

/**
 * 新建任务
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 
 */
public interface ICustomerIcService {
	
	CustomerIndustryCommerce queryObject(Integer id);
	
	void save(CustomerIndustryCommerce CustomerIndustryCommerce);
	
	int update(CustomerIndustryCommerce CustomerIndustryCommerce);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}

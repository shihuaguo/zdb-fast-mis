package com.zdb.modules.customer.service;

import com.zdb.modules.customer.entity.CustomerTax;

/**
 * 新建任务
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 
 */
public interface ICustomerTaxService {
	
	CustomerTax queryObject(Integer id);
	
	void save(CustomerTax customerTax);
	
	int update(CustomerTax customerTax);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
}

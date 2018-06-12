package com.zdb.modules.customer.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.PageInfo;
import com.zdb.modules.customer.entity.Customer;

/**
 * 新建任务
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 
 */
public interface ICustomerService {
	
	Customer queryObject(Integer id);
	
	PageInfo<Customer> queryPage(Map<String, Object> map, int page, int rows);
	
	List<Customer> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	List<Customer> queryListWithIc(Map<String, Object> map);
	
	int queryTotalWithIc(Map<String, Object> map);
	
	List<Customer> queryListWithTax(Map<String, Object> map);
	
	int queryTotalWithTax(Map<String, Object> map);
	
	void save(Customer customer);
	
	int update(Customer customer);
	
	void delete(Integer id);
	
	void deleteBatch(Integer[] ids);
	
	List<Customer> queryByCustomerNo(Map<String, Object> map);
	
	List<String> queryNameList(Map<String, Object> map);
	
	//查询check_login_state字段
	String queryCheckLoginStateByPrimaryKey(@Param("customerId") Integer customerId);
	
	List<Customer> queryListWithIcTax(Map<String, Object> map);
}

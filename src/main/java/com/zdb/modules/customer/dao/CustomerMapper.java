package com.zdb.modules.customer.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.zdb.modules.customer.entity.Customer;

@Mapper
public interface CustomerMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(Customer record);

	int insertSelective(Customer record);

	Customer selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(Customer record);

	int updateByPrimaryKey(Customer record);

	List<Customer> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);
	
	int deleteBatch(Integer[] ids);
	
	List<Customer> queryListWithIc(Map<String, Object> map);
	
	int queryTotalWithIc(Map<String, Object> map);
	
	List<Customer> queryListWithTax(Map<String, Object> map);
	
	int queryTotalWithTax(Map<String, Object> map);
	
	List<Customer> queryByCustomerNo(Map<String, Object> map);
	
	List<String> queryNameList(Map<String, Object> map);
	
	List<Customer> queryListWithIcTax(Map<String, Object> map);
}
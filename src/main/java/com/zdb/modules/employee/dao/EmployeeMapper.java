package com.zdb.modules.employee.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EmployeeMapper {
	
	void saveEmployeeCustomerR(@Param("userId") Integer userId, @Param("selCustomerIds") String[] selCustomerIds);
	
	void deleteByUserId(@Param("userId") Integer userId);
	
	/**
	 * 根据员工id和客户ids删除
	 * @param userId
	 * @param customerIds
	 */
	void deleteByUidAndCids(@Param("userId") Integer userId, @Param("customerIds") String[] customerIds);
	
	List<String> findCustomerIds(@Param("userId") Integer userId);
}

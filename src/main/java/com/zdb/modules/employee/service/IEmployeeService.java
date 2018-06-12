package com.zdb.modules.employee.service;

import java.util.List;

public interface IEmployeeService {

	/**
	 * 保存员工和客户关系,保存时,先删除员工和当前页的客户的关系,再插入,并不影响其他页的员工和客户关系
	 * @param userId			员工id
	 * @param selCustomerIds	已选择的客户id
	 * @param customerIds		当前页的客户id
	 */
	void saveEmployeeCustomerR(Integer userId, String[] selCustomerIds, String[] customerIds);
	
	/**
	 * 查询员工所分配的客户id
	 * @param userId
	 * @return
	 */
	List<String> findCustomerIds(Integer userId);
}

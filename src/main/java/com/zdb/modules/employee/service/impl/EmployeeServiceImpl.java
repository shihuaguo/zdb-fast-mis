package com.zdb.modules.employee.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdb.modules.employee.dao.EmployeeMapper;
import com.zdb.modules.employee.service.IEmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements IEmployeeService {
	
	@Autowired
	private EmployeeMapper mapper;

	@Override
	@Transactional
	public void saveEmployeeCustomerR(Integer userId, String[] selCustomerIds, String[] customerIds) {
		if(customerIds != null && customerIds.length > 0) {
			mapper.deleteByUidAndCids(userId, customerIds);
		}
		if(selCustomerIds != null && selCustomerIds.length > 0) {
			mapper.saveEmployeeCustomerR(userId, selCustomerIds);
		}
	}

	@Override
	public List<String> findCustomerIds(Integer userId) {
		return mapper.findCustomerIds(userId);
	}

}

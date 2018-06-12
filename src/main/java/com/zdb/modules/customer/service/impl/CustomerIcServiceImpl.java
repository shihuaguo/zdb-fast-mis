package com.zdb.modules.customer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdb.modules.customer.dao.CustomerIndustryCommerceMapper;
import com.zdb.modules.customer.entity.CustomerIndustryCommerce;
import com.zdb.modules.customer.service.ICustomerIcService;

@Service("customerIcService")
public class CustomerIcServiceImpl implements ICustomerIcService {
	
	@Autowired
	private CustomerIndustryCommerceMapper mapper;

	@Override
	public CustomerIndustryCommerce queryObject(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public void save(CustomerIndustryCommerce CustomerIndustryCommerce) {
		mapper.insertSelective(CustomerIndustryCommerce);
	}

	@Override
	public int update(CustomerIndustryCommerce CustomerIndustryCommerce) {
		return mapper.updateByPrimaryKeySelective(CustomerIndustryCommerce);
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

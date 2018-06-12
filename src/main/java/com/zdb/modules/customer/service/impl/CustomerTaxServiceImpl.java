package com.zdb.modules.customer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdb.modules.customer.dao.CustomerTaxMapper;
import com.zdb.modules.customer.entity.CustomerTax;
import com.zdb.modules.customer.service.ICustomerTaxService;

@Service("customerTaxService")
public class CustomerTaxServiceImpl implements ICustomerTaxService {
	
	@Autowired
	private CustomerTaxMapper mapper;

	@Override
	public CustomerTax queryObject(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public void save(CustomerTax customerTax) {
		mapper.insertSelective(customerTax);
	}

	@Override
	public int update(CustomerTax customerTax) {
		return mapper.updateByPrimaryKeySelective(customerTax);
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

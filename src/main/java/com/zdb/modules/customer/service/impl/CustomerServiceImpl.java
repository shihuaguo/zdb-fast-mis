package com.zdb.modules.customer.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zdb.modules.customer.dao.CustomerIndustryCommerceMapper;
import com.zdb.modules.customer.dao.CustomerMapper;
import com.zdb.modules.customer.dao.CustomerTaxMapper;
import com.zdb.modules.customer.entity.Customer;
import com.zdb.modules.customer.entity.CustomerIndustryCommerce;
import com.zdb.modules.customer.entity.CustomerTax;
import com.zdb.modules.customer.service.ICustomerService;

@Service("customerService")
public class CustomerServiceImpl implements ICustomerService {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
	
	@Autowired
	private CustomerMapper mapper;
	@Autowired
	private CustomerIndustryCommerceMapper customerIcMapper;
	@Autowired
	private CustomerTaxMapper customerTaxMapper;

	@Override
	public Customer queryObject(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public PageInfo<Customer> queryPage(Map<String, Object> map, int page, int rows) {
		PageHelper.startPage(page, rows);
		List<Customer> list = mapper.queryList(map);
		return new PageInfo<>(list);
	}

	@Override
	public List<Customer> queryList(Map<String, Object> map) {
		return mapper.queryList(map);
	}

	@Override
	public List<Customer> queryListWithIc(Map<String, Object> map) {
		return mapper.queryListWithIc(map);
	}

	@Override
	public int queryTotalWithIc(Map<String, Object> map) {
		return mapper.queryTotalWithIc(map);
	}

	@Override
	public List<Customer> queryListWithTax(Map<String, Object> map) {
		return mapper.queryListWithTax(map);
	}

	@Override
	public int queryTotalWithTax(Map<String, Object> map) {
		return mapper.queryTotalWithTax(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return mapper.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(Customer customer) {
		logger.info("插入客户信息:");
		mapper.insertSelective(customer);
		if(customer.getCustomerIndCom() != null) {
			logger.info("插入工商登记信息,customer id={}", customer.getId());
			CustomerIndustryCommerce customerIc = customer.getCustomerIndCom();
			customerIc.setCustomerId(customer.getId());
			customerIcMapper.insertSelective(customerIc);
		}
		if(customer.getCustomerTax() != null) {
			logger.info("插入税务登记信息,customer id={}", customer.getId());
			CustomerTax customerTax = customer.getCustomerTax();
			customerTax.setCustomerId(customer.getId());
			customerTaxMapper.insertSelective(customerTax);
		}
	}

	@Override
	public int update(Customer customer) {
		logger.info("修改客户信息:");
		//更新客户信息表的统一代码为税务信息的国税税号
		if(customer.getCustomerTax() != null) {
			if(StringUtils.isNotBlank(customer.getCustomerTax().getNationalTaxNumber()));
			customer.setTaxIdNumber(customer.getCustomerTax().getNationalTaxNumber());
		}
		int n = mapper.updateByPrimaryKeySelective(customer);
		if(n > 0) {
			if(customer.getCustomerIndCom() != null) {
				logger.info("修改工商登记信息,customer id={}", customer.getId());
				CustomerIndustryCommerce customerIc = customer.getCustomerIndCom();
				customerIc.setCustomerId(customer.getId());
				if(customerIcMapper.selectByPrimaryKey(customer.getId()) != null) {
					customerIcMapper.updateByPrimaryKeySelective(customerIc);
				}else {
					customerIcMapper.insertSelective(customerIc);
				}
			}
			if(customer.getCustomerTax() != null) {
				logger.info("修改税务登记信息,customer id={}", customer.getId());
				CustomerTax customerTax = customer.getCustomerTax();
				customerTax.setCustomerId(customer.getId());
				if(customerTaxMapper.selectByPrimaryKey(customer.getId()) != null) {
					customerTaxMapper.updateByPrimaryKeySelective(customerTax);
				}else {
					customerTaxMapper.insertSelective(customerTax);
				}
			}
		}
		return n;
	}

	@Override
	public void delete(Integer id) {
		mapper.deleteByPrimaryKey(id);
	}

	@Override
	@Transactional
	public void deleteBatch(Integer[] ids) {
//		mapper.deleteBatch(ids);
//		customerIcMapper.deleteBatch(ids);
//		customerTaxMapper.deleteBatch(ids);
		//修改删除逻辑,修改status状态标记
		mapper.deleteBatch(ids);
	}

	@Override
	public List<Customer> queryByCustomerNo(Map<String, Object> map) {
		return mapper.queryByCustomerNo(map);
	}

	@Override
	public List<String> queryNameList(Map<String, Object> map) {
		return mapper.queryNameList(map);
	}

	@Override
	public String queryCheckLoginStateByPrimaryKey(Integer customerId) {
		return customerTaxMapper.queryCheckLoginStateByPrimaryKey(customerId);
	}

	@Override
	public List<Customer> queryListWithIcTax(Map<String, Object> map) {
		return mapper.queryListWithIcTax(map);
	}

}

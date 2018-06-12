package com.zdb.modules.customer.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.zdb.modules.customer.entity.CustomerTax;

@Mapper
public interface CustomerTaxMapper {
    int deleteByPrimaryKey(Integer customerId);

    int insert(CustomerTax record);

    int insertSelective(CustomerTax record);

    CustomerTax selectByPrimaryKey(Integer customerId);

    int updateByPrimaryKeySelective(CustomerTax record);

    int updateByPrimaryKey(CustomerTax record);

	void deleteBatch(Integer[] ids);
	
	//查询check_login_state字段
	String queryCheckLoginStateByPrimaryKey(@Param("customerId") Integer customerId);
}
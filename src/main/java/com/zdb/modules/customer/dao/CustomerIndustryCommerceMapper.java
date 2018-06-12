package com.zdb.modules.customer.dao;

import org.apache.ibatis.annotations.Mapper;

import com.zdb.modules.customer.entity.CustomerIndustryCommerce;

@Mapper
public interface CustomerIndustryCommerceMapper {
    int deleteByPrimaryKey(Integer customerId);

    int insert(CustomerIndustryCommerce record);

    int insertSelective(CustomerIndustryCommerce record);

    CustomerIndustryCommerce selectByPrimaryKey(Integer customerId);

    int updateByPrimaryKeySelective(CustomerIndustryCommerce record);

    int updateByPrimaryKey(CustomerIndustryCommerce record);
    
    void deleteBatch(Integer[] ids);
}
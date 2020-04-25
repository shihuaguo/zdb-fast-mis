package com.zdb.modules.crawl.entity;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (CrawlCompanyInfo)实体类
 *
 * @author makejava
 * @since 2020-04-23 23:02:35
 */
@Data
@Builder
public class CrawlCompanyInfo implements Serializable {
    private static final long serialVersionUID = 697076651418531789L;
    /**
    * 主键
    */
    private Long id;
    /**
    * 企业公司信息 json格式
    */
    private String companyInfo;
    /**
    * 创建时间
    */
    private Date createTime;
    /**
    * 修改时间
    */
    private Date updateTime;
    /**
    * 版本号
    */
    private Integer version;
    /**
    * 删除标识：0，否，1是
    */
    private Object flag;



}
package com.zdb.modules.crawl.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (CrawlCompanyInfo)实体类
 *
 * @author makejava
 * @since 2020-04-23 23:02:35
 */
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(String companyInfo) {
        this.companyInfo = companyInfo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Object getFlag() {
        return flag;
    }

    public void setFlag(Object flag) {
        this.flag = flag;
    }

}
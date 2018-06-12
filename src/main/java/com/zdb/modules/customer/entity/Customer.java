package com.zdb.modules.customer.entity;

import java.util.Date;

import com.zdb.common.utils.excel.ExcelCell;
import com.zdb.common.utils.excel.Exportable;

public class Customer implements Exportable{
    private Integer id;

    @ExcelCell(index=1,cname="客户编号")
    private String customerNo;

    @ExcelCell(index=2,cname="客户名称")
    private String customerName;

    @ExcelCell(index=3,cname="区域")
    private String region;

    @ExcelCell(index=4,cname="统一代码")
    private String taxIdNumber;

    @ExcelCell(index=5,cname="法人")
    private String legalPerson;

    @ExcelCell(index=6,cname="法人身份证号")
    private String legalPersonId;

    @ExcelCell(index=7,cname="开户银行")
    private String bankName;

    @ExcelCell(index=8,cname="开户银行账号")
    private String bankAccount;

    private String bankPhone;

    private String remark;
    
    private Byte status;

    private Date createTime;

    private Date updateTime;
    
    @ExcelCell(index=9,cname="工商信息")
    private CustomerIndustryCommerce customerIndCom;
    @ExcelCell(index=10,cname="税务信息")
    private CustomerTax customerTax;
    
    /*@ExcelCell(index=9,cname="法人账号")
    public String getLegalPersonAccount() {
    	return customerTax == null ? null : customerTax.getLegalPersonAccount();
    }
    
    @ExcelCell(index=10,cname="法人密码")
    public String getLegalPersonPassword() {
    	return customerTax == null ? null : customerTax.getLegalPersonPassword();
    }*/
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo == null ? null : customerNo.trim();
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getTaxIdNumber() {
        return taxIdNumber;
    }

    public void setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber == null ? null : taxIdNumber.trim();
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson == null ? null : legalPerson.trim();
    }

    public String getLegalPersonId() {
        return legalPersonId;
    }

    public void setLegalPersonId(String legalPersonId) {
        this.legalPersonId = legalPersonId == null ? null : legalPersonId.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getBankPhone() {
        return bankPhone;
    }

    public void setBankPhone(String bankPhone) {
        this.bankPhone = bankPhone == null ? null : bankPhone.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
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

	public CustomerIndustryCommerce getCustomerIndCom() {
		return customerIndCom;
	}

	public void setCustomerIndCom(CustomerIndustryCommerce customerIndCom) {
		this.customerIndCom = customerIndCom;
	}

	public CustomerTax getCustomerTax() {
		return customerTax;
	}

	public void setCustomerTax(CustomerTax customerTax) {
		this.customerTax = customerTax;
	}
}
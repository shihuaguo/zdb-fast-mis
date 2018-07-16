package com.zdb.modules.customer.entity;

import java.util.Date;

import com.zdb.common.utils.excel.ExcelCell;
import com.zdb.common.utils.excel.Exportable;

public class CustomerIndustryCommerce implements Exportable{
    private Integer customerId;

    @ExcelCell(index=1,cname="社会信用代码")
    private String socialReditOde;

    //@ExcelCell(index=2,cname="工商注册号")
    private String taxIdNumber;

    private String legalPerson;

    private String legalPersonId;

    @ExcelCell(index=3,cname="注册地址")
    private String registerAddr;

    @ExcelCell(index=4,cname="注册资本")
    private String registerCapital;
    
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
//    private Date startDate;
    
    @ExcelCell(index=5,cname="成立日期")
    private String startDate;
    
    @ExcelCell(index=6,cname="营业期限")
    private String businessTerm;

    @ExcelCell(index=7,cname="股东信息")
    private String shareholder;

    @ExcelCell(index=8,cname="工商登陆账号")
    private String industryCommerceAccount;

    @ExcelCell(index=9,cname="工商登陆密码")
    private String industryCommercePwd;

    @ExcelCell(index=10,cname="年度报告情况")
    private String annualReport;
    
    @ExcelCell(index=11,cname="状态")
    private String businessStatus;
    
    @ExcelCell(index=12,cname="异常信息")
    private String abnormalList;

    private String printReceiptPassword;

    private String remark;

    private Date createTime;

    private Date updateTime;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getSocialReditOde() {
        return socialReditOde;
    }

    public void setSocialReditOde(String socialReditOde) {
        this.socialReditOde = socialReditOde == null ? null : socialReditOde.trim();
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

    public String getRegisterAddr() {
        return registerAddr;
    }

    public void setRegisterAddr(String registerAddr) {
        this.registerAddr = registerAddr == null ? null : registerAddr.trim();
    }

    public String getRegisterCapital() {
        return registerCapital;
    }

    public void setRegisterCapital(String registerCapital) {
        this.registerCapital = registerCapital == null ? null : registerCapital.trim();
    }

    public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getBusinessTerm() {
		return businessTerm;
	}

	public void setBusinessTerm(String businessTerm) {
		this.businessTerm = businessTerm;
	}

	public String getShareholder() {
        return shareholder;
    }

    public void setShareholder(String shareholder) {
        this.shareholder = shareholder == null ? null : shareholder.trim();
    }

    public String getIndustryCommerceAccount() {
        return industryCommerceAccount;
    }

    public void setIndustryCommerceAccount(String industryCommerceAccount) {
        this.industryCommerceAccount = industryCommerceAccount == null ? null : industryCommerceAccount.trim();
    }

    public String getIndustryCommercePwd() {
        return industryCommercePwd;
    }

    public void setIndustryCommercePwd(String industryCommercePwd) {
        this.industryCommercePwd = industryCommercePwd == null ? null : industryCommercePwd.trim();
    }

    public String getAnnualReport() {
        return annualReport;
    }

    public void setAnnualReport(String annualReport) {
        this.annualReport = annualReport == null ? null : annualReport.trim();
    }

    public String getPrintReceiptPassword() {
        return printReceiptPassword;
    }

    public void setPrintReceiptPassword(String printReceiptPassword) {
        this.printReceiptPassword = printReceiptPassword == null ? null : printReceiptPassword.trim();
    }

    public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String businessStatus) {
		this.businessStatus = businessStatus;
	}

	public String getAbnormalList() {
		return abnormalList;
	}

	public void setAbnormalList(String abnormalList) {
		this.abnormalList = abnormalList;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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
}
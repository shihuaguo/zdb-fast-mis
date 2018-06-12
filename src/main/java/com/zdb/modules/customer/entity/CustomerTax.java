package com.zdb.modules.customer.entity;

import java.util.Date;

import com.zdb.common.utils.excel.ExcelCell;
import com.zdb.common.utils.excel.Exportable;

public class CustomerTax implements Exportable{
    private Integer customerId;

    @ExcelCell(index=1,cname="国税税号")
    private String nationalTaxNumber;

    @ExcelCell(index=2,cname="地税税号")
    private String localTaxNumber;

    @ExcelCell(index=3,cname="地税纳税人编码")
    private String localTaxCode;

    @ExcelCell(index=4,cname="财务负责人")
    private String financialController;

    @ExcelCell(index=5,cname="办税员")
    private String taxAgent;

    @ExcelCell(index=6,cname="购票员")
    private String ticketAgent;

    @ExcelCell(index=6,cname="投资信息")
    private String investorInfo;

    @ExcelCell(index=7,cname="主管国税")
    private String nationalTaxDpt;

    @ExcelCell(index=8,cname="主管地税")
    private String localTaxDpt;

    @ExcelCell(index=9,cname="法人实名账号")
    private String legalPersonAccount;

    @ExcelCell(index=10,cname="法人账号密码")
    private String legalPersonPassword;

    @ExcelCell(index=11,cname="地税登陆账号")
    private String localTaxAccount;

    @ExcelCell(index=12,cname="地税登陆账号密码")
    private String localTaxPwd;

    private String remark;

    @ExcelCell(index=13,cname="原国税一号窗密码")
    private String oldNationalTaxPwd;

    private String remark1;

    @ExcelCell(index=14,cname="办税人姓名")
    private String bsrxm;

    @ExcelCell(index=15,cname="办税人电话")
    private String bsryddh;

    @ExcelCell(index=16,cname="办税人证件号码")
    private String bsrzjhm;

    @ExcelCell(index=17,cname="财务负责人姓名")
    private String cwfzrxm;

    @ExcelCell(index=18,cname="财务负责人电话")
    private String cwfzryddh;

    @ExcelCell(index=19,cname="财务负责人证件号码")
    private String cwfzrzjhm;

    @ExcelCell(index=20,cname="法定代表人")
    private String fddbrxm;

    @ExcelCell(index=21,cname="法定代表人电话")
    private String fddbryddh;

    @ExcelCell(index=22,cname="法定代表人证件号码")
    private String fddbrzjhm;
    
    private String checkLoginState;

    private Date createTime;

    private Date updateTime;

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getNationalTaxNumber() {
        return nationalTaxNumber;
    }

    public void setNationalTaxNumber(String nationalTaxNumber) {
        this.nationalTaxNumber = nationalTaxNumber == null ? null : nationalTaxNumber.trim();
    }

    public String getLocalTaxNumber() {
        return localTaxNumber;
    }

    public void setLocalTaxNumber(String localTaxNumber) {
        this.localTaxNumber = localTaxNumber == null ? null : localTaxNumber.trim();
    }

    public String getLocalTaxCode() {
        return localTaxCode;
    }

    public void setLocalTaxCode(String localTaxCode) {
        this.localTaxCode = localTaxCode == null ? null : localTaxCode.trim();
    }

    public String getFinancialController() {
        return financialController;
    }

    public void setFinancialController(String financialController) {
        this.financialController = financialController == null ? null : financialController.trim();
    }

    public String getTaxAgent() {
        return taxAgent;
    }

    public void setTaxAgent(String taxAgent) {
        this.taxAgent = taxAgent == null ? null : taxAgent.trim();
    }

    public String getTicketAgent() {
        return ticketAgent;
    }

    public void setTicketAgent(String ticketAgent) {
        this.ticketAgent = ticketAgent == null ? null : ticketAgent.trim();
    }

    public String getInvestorInfo() {
        return investorInfo;
    }

    public void setInvestorInfo(String investorInfo) {
        this.investorInfo = investorInfo == null ? null : investorInfo.trim();
    }

    public String getNationalTaxDpt() {
        return nationalTaxDpt;
    }

    public void setNationalTaxDpt(String nationalTaxDpt) {
        this.nationalTaxDpt = nationalTaxDpt == null ? null : nationalTaxDpt.trim();
    }

    public String getLocalTaxDpt() {
        return localTaxDpt;
    }

    public void setLocalTaxDpt(String localTaxDpt) {
        this.localTaxDpt = localTaxDpt == null ? null : localTaxDpt.trim();
    }

    public String getLegalPersonAccount() {
        return legalPersonAccount;
    }

    public void setLegalPersonAccount(String legalPersonAccount) {
        this.legalPersonAccount = legalPersonAccount == null ? null : legalPersonAccount.trim();
    }

    public String getLegalPersonPassword() {
        return legalPersonPassword;
    }

    public void setLegalPersonPassword(String legalPersonPassword) {
        this.legalPersonPassword = legalPersonPassword == null ? null : legalPersonPassword.trim();
    }

    public String getLocalTaxAccount() {
        return localTaxAccount;
    }

    public void setLocalTaxAccount(String localTaxAccount) {
        this.localTaxAccount = localTaxAccount == null ? null : localTaxAccount.trim();
    }

    public String getLocalTaxPwd() {
        return localTaxPwd;
    }

    public void setLocalTaxPwd(String localTaxPwd) {
        this.localTaxPwd = localTaxPwd == null ? null : localTaxPwd.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getOldNationalTaxPwd() {
        return oldNationalTaxPwd;
    }

    public void setOldNationalTaxPwd(String oldNationalTaxPwd) {
        this.oldNationalTaxPwd = oldNationalTaxPwd == null ? null : oldNationalTaxPwd.trim();
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1 == null ? null : remark1.trim();
    }

    public String getBsrxm() {
        return bsrxm;
    }

    public void setBsrxm(String bsrxm) {
        this.bsrxm = bsrxm == null ? null : bsrxm.trim();
    }

    public String getBsryddh() {
        return bsryddh;
    }

    public void setBsryddh(String bsryddh) {
        this.bsryddh = bsryddh == null ? null : bsryddh.trim();
    }

    public String getBsrzjhm() {
        return bsrzjhm;
    }

    public void setBsrzjhm(String bsrzjhm) {
        this.bsrzjhm = bsrzjhm == null ? null : bsrzjhm.trim();
    }

    public String getCwfzrxm() {
        return cwfzrxm;
    }

    public void setCwfzrxm(String cwfzrxm) {
        this.cwfzrxm = cwfzrxm == null ? null : cwfzrxm.trim();
    }

    public String getCwfzryddh() {
        return cwfzryddh;
    }

    public void setCwfzryddh(String cwfzryddh) {
        this.cwfzryddh = cwfzryddh == null ? null : cwfzryddh.trim();
    }

    public String getCwfzrzjhm() {
        return cwfzrzjhm;
    }

    public void setCwfzrzjhm(String cwfzrzjhm) {
        this.cwfzrzjhm = cwfzrzjhm == null ? null : cwfzrzjhm.trim();
    }

    public String getFddbrxm() {
        return fddbrxm;
    }

    public void setFddbrxm(String fddbrxm) {
        this.fddbrxm = fddbrxm == null ? null : fddbrxm.trim();
    }

    public String getFddbryddh() {
        return fddbryddh;
    }

    public void setFddbryddh(String fddbryddh) {
        this.fddbryddh = fddbryddh == null ? null : fddbryddh.trim();
    }

    public String getFddbrzjhm() {
        return fddbrzjhm;
    }

    public void setFddbrzjhm(String fddbrzjhm) {
        this.fddbrzjhm = fddbrzjhm == null ? null : fddbrzjhm.trim();
    }

    public String getCheckLoginState() {
		return checkLoginState;
	}

	public void setCheckLoginState(String checkLoginState) {
		this.checkLoginState = checkLoginState;
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
package com.zdb.modules.cal.entity;

import java.util.Date;

public class CalTaskVerify {
    private Integer id;

    private Integer taskItemId;

    private Byte targetStatus;

    private Byte verifyStatus;

    private Integer applyUser;

    private Integer verifyUser;

    private String applyRemark;

    private String verifyRemark;

    private Integer version;

    private Date createTime;

    private Date updateTime;
    
    //非数据库字段
    private String taskItemTitle;
    private String taskName;
    private String username;
    private String username1;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskItemId() {
        return taskItemId;
    }

    public void setTaskItemId(Integer taskItemId) {
        this.taskItemId = taskItemId;
    }

    public Byte getTargetStatus() {
        return targetStatus;
    }

    public void setTargetStatus(Byte targetStatus) {
        this.targetStatus = targetStatus;
    }

    public Byte getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Byte verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public Integer getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(Integer applyUser) {
        this.applyUser = applyUser;
    }

    public Integer getVerifyUser() {
        return verifyUser;
    }

    public void setVerifyUser(Integer verifyUser) {
        this.verifyUser = verifyUser;
    }

    public String getApplyRemark() {
        return applyRemark;
    }

    public void setApplyRemark(String applyRemark) {
        this.applyRemark = applyRemark == null ? null : applyRemark.trim();
    }

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark == null ? null : verifyRemark.trim();
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

	public String getTaskItemTitle() {
		return taskItemTitle;
	}

	public void setTaskItemTitle(String taskItemTitle) {
		this.taskItemTitle = taskItemTitle;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername1() {
		return username1;
	}

	public void setUsername1(String username1) {
		this.username1 = username1;
	}
}
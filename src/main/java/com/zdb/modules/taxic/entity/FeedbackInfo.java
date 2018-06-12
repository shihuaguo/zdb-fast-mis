package com.zdb.modules.taxic.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.zdb.common.validator.group.AddGroup;
import com.zdb.common.validator.group.UpdateGroup;

public class FeedbackInfo {
    private Integer id;

    @NotNull(message="信息反馈类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String type1;

    private String type2;

    @NotNull(message="信息反馈不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String feedbackInfo;

    private String workSummary;

    private Integer createBy;

    private Integer updateBy;

    private Integer version;

    private Date feedbackTime;
    
    private Date createTime;

    private Date updateTime;
    
    private String username;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1 == null ? null : type1.trim();
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2 == null ? null : type2.trim();
    }

    public String getFeedbackInfo() {
        return feedbackInfo;
    }

    public void setFeedbackInfo(String feedbackInfo) {
        this.feedbackInfo = feedbackInfo == null ? null : feedbackInfo.trim();
    }

    public String getWorkSummary() {
        return workSummary;
    }

    public void setWorkSummary(String workSummary) {
        this.workSummary = workSummary == null ? null : workSummary.trim();
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
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

    public Date getFeedbackTime() {
		return feedbackTime;
	}

	public void setFeedbackTime(Date feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

	public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
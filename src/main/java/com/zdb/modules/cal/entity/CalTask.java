package com.zdb.modules.cal.entity;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.zdb.common.utils.excel.ExcelCell;
import com.zdb.common.validator.group.AddGroup;
import com.zdb.common.validator.group.UpdateGroup;

public class CalTask implements Serializable{
	private static final long serialVersionUID = 1L;

	@ExcelCell(index=1,cname="ID")
	private Integer id;

	@NotNull(message="任务类型不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Byte taskType;

	//@NotBlank(message="任务编号不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@ExcelCell(index=3,cname="编号")
    private String taskNo;

	@NotBlank(message="任务名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@ExcelCell(index=4,cname="任务名称")
    private String taskName;

    private Integer customerId;

    @NotBlank(message="客户名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @ExcelCell(index=5,cname="客户")
    private String customerName;

    private Byte taskClass;

    private Integer taskSubClass;
    
    private String taskClassDetail;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectedTime;

    private Byte status;

    private Byte progress;

    @NotNull(message="办理员工不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer employeeId;
    
    private Long rank;	//置顶时间戳,最新置顶的排在前面

    private Integer version;

    @ExcelCell(index=10,cname="首次录入日期")
    private Date createTime;

    private Date updateTime;
    
    @ExcelCell(index=9,cname="办理人员")
    private String employeeName;	//员工姓名
    
    //Excel导出时的字段
    @ExcelCell(index=2,cname="状态")
    public String getStatusStr() {
    	return status == null ? "未完成" : TaskStatusEnum.getDescByCode(status);
    }
    
    @ExcelCell(index=6,cname="任务大类")
    public String getTaskTypeStr() {
    	return taskType == null ? "" : TaskTypeEnum.getDescByCode(taskType);
    }
    
    @ExcelCell(index=7,cname="任务分类")
    public String getTaskClassStr() {
    	return taskClass == null ? "" : TaskClassEnum.getDescByCode(taskClass);
    }
    
    @ExcelCell(index=8,cname="任务子分类")
    public String getTaskSubClassStr() {
    	return taskSubClass == null ? "" : TaskSubClassEnum.getDescByCode(taskSubClass);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getTaskType() {
        return taskType;
    }

    public void setTaskType(Byte taskType) {
        this.taskType = taskType;
    }

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo == null ? null : taskNo.trim();
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public Byte getTaskClass() {
        return taskClass;
    }

    public void setTaskClass(Byte taskClass) {
        this.taskClass = taskClass;
    }

    public Integer getTaskSubClass() {
        return taskSubClass;
    }

    public void setTaskSubClass(Integer taskSubClass) {
        this.taskSubClass = taskSubClass;
    }

    public String getTaskClassDetail() {
		return taskClassDetail;
	}

	public void setTaskClassDetail(String taskClassDetail) {
		this.taskClassDetail = taskClassDetail;
	}

	public Date getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(Date expectedTime) {
        this.expectedTime = expectedTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getProgress() {
        return progress;
    }

    public void setProgress(Byte progress) {
        this.progress = progress;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}
}
package com.zdb.modules.cal.entity;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.time.DateFormatUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import com.zdb.common.utils.excel.ExcelCell;
import com.zdb.common.validator.group.AddGroup;
import com.zdb.common.validator.group.UpdateGroup;

public class CalTaskItem {
	
	@ExcelCell(index=1,cname="ID")
    private Integer id;

    //@NotNull(message="任务ID不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private Integer taskId;

    private Integer taskItemOrder;

    @ExcelCell(index=3,cname="具体事项")
    private String taskItemTitle;
    
    private Integer status;	//事项状态 0-未完成 1-已完成

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @ExcelCell(index=5,cname="处理时间")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;

    @NotBlank(message="地点不能为空", groups = {AddGroup.class, UpdateGroup.class})
    @ExcelCell(index=6,cname="地点")
    private String place;

    private Integer employeeId;

    private Integer version;

    private Date createTime;

    private Date updateTime;
    
    @ExcelCell(index=7,cname="处理人员")
    private String employeeName;	//员工用户名
    
    @ExcelCell(index=2,cname="所属任务")
    private String taskName;		//所属任务名称
    
  //Excel导出时的字段
    @ExcelCell(index=4,cname="状态")
    public String getStatusStr() {
    	return status == null ? "未完成" : TaskStatusEnum.getDescByCode(status);
    }
    
    public static EventObject convertToEventObject(CalTaskItem taskItem) {
    	EventObject eo = new EventObject();
    	eo.setId(taskItem.getId());
    	eo.setTitle(taskItem.getTaskItemTitle());
    	eo.setStart(DateFormatUtils.format(taskItem.getStartTime(), "yyyy-MM-dd HH:mm:ss"));
    	if(taskItem.getEndTime() != null) {
    		eo.setEnd(DateFormatUtils.format(taskItem.getEndTime(), "yyyy-MM-dd HH:mm:ss"));
    	}
    	eo.setAllDay(false);
    	eo.setStatus(taskItem.getStatus());
    	
    	eo.setTaskItemTitle(taskItem.getTaskItemTitle());
    	eo.setEmployeeName(taskItem.getEmployeeName());
    	eo.setPlace(taskItem.getPlace());
    	eo.setTaskName(taskItem.getTaskName());
    	return eo;
    }
    
    public static List<EventObject> convertToEventObjectList(List<CalTaskItem> list) {
    	return list.stream().map(CalTaskItem::convertToEventObject).collect(Collectors.toList());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskItemOrder() {
        return taskItemOrder;
    }

    public void setTaskItemOrder(Integer taskItemOrder) {
        this.taskItemOrder = taskItemOrder;
    }

    public String getTaskItemTitle() {
        return taskItemTitle;
    }

    public void setTaskItemTitle(String taskItemTitle) {
        this.taskItemTitle = taskItemTitle == null ? null : taskItemTitle.trim();
    }

    public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getHandleTime() {
        return handleTime;
    }

    public void setHandleTime(Date handleTime) {
        this.handleTime = handleTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place == null ? null : place.trim();
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

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
}
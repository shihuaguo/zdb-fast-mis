package com.zdb.modules.cal.entity;

/**
 * A standard JavaScript object that FullCalendar uses to store information about a calendar event. 
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2017年9月4日
 */
public class EventObject {

	private Integer id;
	private String title;
	private Boolean allDay;
	private String start;
	private String end;
	
	private String taskItemTitle;
	private String place;
	private String employeeName;
	
	private Integer status;	//事项状态
	
	private String taskName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Boolean getAllDay() {
		return allDay;
	}
	public void setAllDay(Boolean allDay) {
		this.allDay = allDay;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getTaskItemTitle() {
		return taskItemTitle;
	}
	public void setTaskItemTitle(String taskItemTitle) {
		this.taskItemTitle = taskItemTitle;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
}

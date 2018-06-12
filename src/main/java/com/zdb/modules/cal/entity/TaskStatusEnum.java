package com.zdb.modules.cal.entity;

public enum TaskStatusEnum {
	
	UN_FINISHED(0,"未完成"),
	FINISHED(1,"已完成");

	int code;
	String desc;
	
	TaskStatusEnum(int code, String desc){
		this.code = code;
		this.desc = desc;
	}

	public int getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
	
	public static String getDescByCode(int code) {
		for(TaskStatusEnum e : TaskStatusEnum.values()) {
			if(e.code == code) {
				return e.getDesc();
			}
		}
		return UN_FINISHED.getDesc();
	}
}

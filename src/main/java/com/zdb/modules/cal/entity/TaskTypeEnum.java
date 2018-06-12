package com.zdb.modules.cal.entity;

/**
 * 任务类型枚举
 * 
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2018年2月2日
 */
public enum TaskTypeEnum {
	
	IC(1,"工商"),
	TAX(2,"税务"),
	OTHER(9,"其他");

	int code;
	String desc;
	
	TaskTypeEnum(int code, String desc){
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
		for(TaskTypeEnum e : TaskTypeEnum.values()) {
			if(e.code == code) {
				return e.getDesc();
			}
		}
		return OTHER.getDesc();
	}
}

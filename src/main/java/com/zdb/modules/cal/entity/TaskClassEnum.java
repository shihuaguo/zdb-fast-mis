package com.zdb.modules.cal.entity;

/**
 * 任务分类枚举
 * 
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2018年2月2日
 */
public enum TaskClassEnum {
	
	KAIYE(11,"开业"),
	BIANGENG(12,"变更"),
	ZHUXIAO(13,"注销"),
	OTHER_1(19,"其他"),
	
	FAPIAO(21,"发票"),
	SHUIWUDENGJI(22,"税务登记"),
	SHENBAO(23,"申报"),
	WENSHUSHOULI(24,"文书受理"),
	OTHER_2(25,"其他"),
	OTHER(99,"其他");

	int code;
	String desc;
	
	TaskClassEnum(int code, String desc){
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
		for(TaskClassEnum e : TaskClassEnum.values()) {
			if(e.code == code) {
				return e.getDesc();
			}
		}
		return OTHER.getDesc();
	}
}

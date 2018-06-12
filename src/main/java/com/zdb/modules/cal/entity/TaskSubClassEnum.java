package com.zdb.modules.cal.entity;

/**
 * 任务子分类枚举
 * 
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2018年2月2日
 */
public enum TaskSubClassEnum {
	
	LINGFAPIAO(211,"领发票"),
	DAIKAIFAPIAO(212,"代开发票"),
	GUOSHUIBAODAO(221,"国税报到"),
	DISHUIBAODAO(222,"地税报到"),
	
	KAITONGSHEBAO(223,"开通社保"),
	BIANGENG(224,"变更"),
	ZHUXIAO(225,"注销"),
	
	
	QIANTAIBAODAO(231,"前台申报"),
	GENGZHENGSHENBAO(232,"更正申报"),
	BAOZHONGHEDING(241,"票种核定"),
	SHENQINGYIBANNASHUIREN(242,"申请一般纳税人（含最好开票限额）"),
	NASHUIZHENGMING(243,"纳税证明"),
	OTHER(99,"其他");

	int code;
	String desc;
	
	TaskSubClassEnum(int code, String desc){
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
		for(TaskSubClassEnum e : TaskSubClassEnum.values()) {
			if(e.code == code) {
				return e.getDesc();
			}
		}
		return OTHER.getDesc();
	}
}

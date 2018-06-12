package com.zdb.modules.ueditor;

/**
 * 请求返回信息
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2018年2月22日
 */
public class Response {
	
	public static final String STATE_SUCCESS = "SUCCESS";

	private String name;
	private String original;
	private String size;
	private String state;
	private String type;
	private String url;
	
	public Response(String name, String original, String size, String state, String type, String url) {
		this.name = name;
		this.original = original;
		this.size = size;
		this.state = state;
		this.type = type;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

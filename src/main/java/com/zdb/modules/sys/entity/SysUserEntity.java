package com.zdb.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.zdb.common.validator.group.AddGroup;
import com.zdb.common.validator.group.UpdateGroup;
import lombok.Data;

/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:28:55
 */
@Data
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long userId;

	@NotBlank(message="用户名不能为空", groups = {AddGroup.class, UpdateGroup.class})
	private String username;

	@NotBlank(message="密码不能为空", groups = AddGroup.class)
	private String password;

	private String salt;

	@NotBlank(message="邮箱不能为空", groups = {AddGroup.class, UpdateGroup.class})
	@Email(message="邮箱格式不正确", groups = {AddGroup.class, UpdateGroup.class})
	private String email;

	private String mobile;

	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer status;

	private String wxOpenid;			//微信用户的唯一标识
	private String wxSessionKey;		//微信用户的会话密钥
	private String wxUnionid;			//用户在微信开放平台的唯一标识符。本字段在满足一定条件的情况下才返回。

	/**
	 * 角色ID列表
	 */
	private List<Long> roleIdList;
	
	/**
	 * 创建者ID
	 */
	private Long createUserId;

	/**
	 * 创建时间
	 */
	private Date createTime;
}

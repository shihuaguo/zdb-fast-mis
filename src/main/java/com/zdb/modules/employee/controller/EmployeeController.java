package com.zdb.modules.employee.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zdb.common.utils.PageUtils;
import com.zdb.common.utils.Query;
import com.zdb.common.utils.R;
import com.zdb.modules.employee.service.IEmployeeService;
import com.zdb.modules.sys.controller.AbstractController;
import com.zdb.modules.sys.entity.SysUserEntity;
import com.zdb.modules.sys.service.SysUserService;

@RestController
@RequestMapping("/employee")
public class EmployeeController extends AbstractController {
	
	private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);
	
	@Autowired
	private SysUserService sysUserService;
	
	@Autowired
	private IEmployeeService employeeService;
	
	/**
	 * 所有员工列表,将管理员角色排除
	 */
	@RequestMapping("/employee_list")
	@RequiresPermissions("employee:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils pageUtil = queryList(params);
		return R.ok().put("page", pageUtil);
	}
	
	protected PageUtils queryList(Map<String, Object> params){
		logger.info("查询员工列表,params={}", params);
		//将管理员排除
		params.put("notAdmin", "1");
		//查询列表数据
		Query query = new Query(params);
		List<SysUserEntity> userList = sysUserService.queryList(query);
		int total = sysUserService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
		return pageUtil;
	}
	
	//保存员工和客户关系
	@RequestMapping("/save_employee_customer_r")
	@RequiresPermissions("employee:save")
	public R saveEmployeeCustomerR(String userId, String selCustomerIds, String customerIds) {
		logger.info("userId={}, selCustomerIds={}, customerIds={}", userId, selCustomerIds, customerIds);
		Integer uid = Integer.parseInt(userId);
		String[] selIds = StringUtils.isBlank(selCustomerIds) ? null : selCustomerIds.split(",");
		String[] ids = StringUtils.isBlank(customerIds) ? null : customerIds.split(",");
		employeeService.saveEmployeeCustomerR(uid, selIds, ids);
		return R.ok();
	}
	
	//根据员工查找客户
	@RequestMapping("/customer_list")
	@RequiresPermissions("employee:list")
	public R getByUserId(Integer userId) {
		List<String> list = employeeService.findCustomerIds(userId);
		return R.ok().put("list", list);
	}
}

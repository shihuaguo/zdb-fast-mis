package com.zdb.modules.cal.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.zdb.common.annotation.SysLog;
import com.zdb.common.exception.RRException;
import com.zdb.common.utils.DateUtils;
import com.zdb.common.utils.PageUtils;
import com.zdb.common.utils.Query;
import com.zdb.common.utils.R;
import com.zdb.common.validator.ValidatorUtils;
import com.zdb.common.validator.group.AddGroup;
import com.zdb.common.validator.group.UpdateGroup;
import com.zdb.modules.cal.entity.CalTask;
import com.zdb.modules.cal.entity.CalTaskItem;
import com.zdb.modules.cal.entity.CalTaskVerify;
import com.zdb.modules.cal.entity.TaskStatusEnum;
import com.zdb.modules.cal.service.ICalTaskItemService;
import com.zdb.modules.cal.service.ICalTaskService;
import com.zdb.modules.sys.controller.AbstractController;
import com.zdb.modules.sys.entity.SysUserEntity;
import com.zdb.modules.sys.service.SysUserService;

/**
 * 任务记录
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2017年8月31日
 */
@RestController
@RequestMapping("/cal/task")
public class CalTaskController extends AbstractController {
	
	@Autowired
	private ICalTaskService calTaskService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private ICalTaskItemService taskItemService;
	@Autowired
	private SysUserService userService;
	
	protected void fuzzlyQuery(Map<String, Object> params) {
		String taskName = (String) params.get("taskName");
		if(StringUtils.isNotBlank(taskName)) {
			params.put("taskName", "%" + taskName + "%");
		}
		String taskItemTitle = (String) params.get("taskItemTitle");
		if(StringUtils.isNotBlank(taskItemTitle)) {
			params.put("taskItemTitle", "%" + taskItemTitle + "%");
		}
	}
	
	/**
	 * 所有任务列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("cal:task:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils pageUtil = queryList(params);
		return R.ok().put("page", pageUtil);
	}
	
	protected PageUtils queryList(Map<String, Object> params){
		logger.info("查询任务列表,params={}", params);
		//查询列表数据
		fuzzlyQuery(params);
		Integer userId = getUserId().intValue();
		//如果是超级管理员或者有管理员角色,则可以查看所有任务
		if(userId > 1 && !sysUserService.hasAdminRole(userId.longValue())) {
			params.put("employeeId", userId);
		}
		Query query = new Query(params);
		List<CalTask> taskList = calTaskService.queryList(query);
		int total = calTaskService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(taskList, total, query.getLimit(), query.getPage());
		return pageUtil;
	}
	
	/**
	 * 导出到Excel
	 * @param params
	 * @param res
	 */
	@RequestMapping("/export")
	@RequiresPermissions("cal:task:save")
	public void export(@RequestParam Map<String, Object> params, HttpServletRequest req, HttpServletResponse res) {
		String fileName = "任务信息导出_" + DateUtils.format(new Date(), "yyyy-MM-dd") + ".xls";
		export(params, CalTask.class, fileName, req, res);
	}
	
	/**
	 * 根据状态查询任务列表
	 */
	@RequestMapping("/list/{status}")
	@RequiresPermissions("cal:task:list")
	public R listByStatus(@PathVariable("status") Byte status){
		//查询列表数据
		Map<String, Object> params = new HashMap<>();
		params.put("status", status);
		List<CalTask> taskList = calTaskService.queryList(params);
		
		return R.ok().put("list", taskList);
	}
	
	/**
	 * 任务名称列表
	 */
	@RequestMapping("/taskNamelist")
	@RequiresPermissions("cal:task:list")
	public Object taskNameList(@RequestParam Map<String, Object> params){
		String term = params.get("term").toString();
		if(StringUtils.isNotBlank(term)) {
//			params.put("term", "%" + term + "%");
			params.put("taskName", "%" + term + "%");
		}
		Integer userId = getUserId().intValue();
		//如果是超级管理员或者有管理员角色,则可以查看所有任务
		if(userId > 1 && !sysUserService.hasAdminRole(userId.longValue())) {
			params.put("employeeId", userId);
		}
		//只查询未完成任务
		params.put("status", 0);
		List<CalTask> taskList = calTaskService.queryList(params);
		List<Map<String, Object>> list = new ArrayList<>();
		for(CalTask task: taskList) {
			Map<String, Object> map = new HashMap<>();
			map.put("label", task.getTaskName());
			map.put("value", task.getId());
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 保存任务
	 */
	@SysLog("保存任务")
	@RequestMapping("/save")
	@RequiresPermissions("cal:task:save")
	public R save(@RequestBody CalTask task){
		ValidatorUtils.validateEntity(task, AddGroup.class);
		calTaskService.save(task);
		return R.ok();
	}
	
	/**
	 * 修改任务
	 */
	@SysLog("修改任务")
	@RequestMapping("/update")
	@RequiresPermissions("cal:task:update")
	public R update(@RequestBody CalTask task){
		ValidatorUtils.validateEntity(task, UpdateGroup.class);
		int n = calTaskService.update(task);
		return n>=1 ? R.ok() : R.error(1, "更新失败");
	}
	
	/**
	 * 修改任务状态
	 */
	@SysLog("修改任务状态")
	@RequestMapping("/updateStatus")
	@RequiresPermissions("cal:task:update")
	public R updateStatus(CalTask task){
		int n = calTaskService.update(task);
		return n>=1 ? R.ok() : R.error(1, "更新失败");
	}
	
	/**
	 * 根据ID获取任务
	 */
	@RequestMapping("/getById/{id}")
//	@RequiresPermissions("cal:task:getById")
	public R info(@PathVariable("id") Integer id){
		CalTask task = calTaskService.queryObject(id);
		List<SysUserEntity> userList = sysUserService.queryListAll();
		
		return R.ok().put("task", task).put("userList", userList);
	}
	
	/**
	 * 删除任务
	 */
	@SysLog("删除任务")
	@RequestMapping("/delete")
	@RequiresPermissions("cal:task:delete")
	public R delete(@RequestBody Integer[] ids){
		calTaskService.deleteBatch(ids);
		return R.ok();
	}
	
	/**
	 * 置顶任务
	 */
	@SysLog("置顶任务")
	@RequestMapping("/top")
	@RequiresPermissions("cal:task:update")
	public R top(Integer id){
		CalTask task = new CalTask();
		task.setId(id);
		task.setRank(new Date().getTime());
		int n = calTaskService.update(task);
		return n > 0 ? R.ok() : R.error("置顶失败");
	}
	
	//////////////////任务事项相关////////////////////////////////////////////////
	
	/**
	 * 根据任务获取事项
	 */
	@RequestMapping("/item/list")
	@RequiresPermissions("cal:task:list")
	public R itemList(@RequestParam Map<String, Object> params){
		Object pageUtil = queryTaskItemList(params);
		return R.ok().put("page", pageUtil);
	}
	
	private PageUtils queryTaskItemList(@RequestParam Map<String, Object> params) {
		logger.info("根据任务获取事项,params={}", params);
		Integer userId = getUserId().intValue();
		//如果是超级管理员或者有管理员角色,则可以查看所有任务
		if(userId > 1 && !sysUserService.hasAdminRole(userId.longValue())) {
			params.put("employeeId", userId);
		}
		Query query = new Query(params);
		List<CalTaskItem> list = taskItemService.queryList(query);
		int total = taskItemService.queryTotal(query);
		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
		return pageUtil;
	}
	
	/**
	 * 导出到Excel
	 * @param params
	 * @param res
	 */
	@RequestMapping("/exportTaskItem")
	@RequiresPermissions("cal:task:save")
	public void exportTaskItem(@RequestParam Map<String, Object> params, HttpServletRequest req, HttpServletResponse res) {
		String fileName = "任务事项信息导出_" + DateUtils.format(new Date(), "yyyy-MM-dd") + ".xls";
		PageUtils pageUtil = queryTaskItemList(params);
		export(pageUtil.getList(), CalTaskItem.class, fileName, req, res);
	}
	
	/**
	 * 保存任务事项
	 */
	@SysLog("保存任务事项")
	@RequestMapping("/item/save")
	@RequiresPermissions("cal:task:save")
	public R saveTaskItem(CalTaskItem taskItem){
		ValidatorUtils.validateEntity(taskItem, AddGroup.class);
		checkParam(taskItem);
		taskItem.setId(null);
		taskItemService.save(taskItem);
		return R.ok();
	}
	
	//检查输入参数
	private void checkParam(CalTaskItem taskItem) {
		String names = taskItem.getEmployeeName();
		if(StringUtils.isNotBlank(names)) {
			String[] ss = names.split(",");
			List<Integer> ids = Arrays.asList(ss).stream().filter(s -> {
				return s != null && !s.trim().equals("");
			}).map(s -> {
				SysUserEntity user = userService.queryByUserName(s);
				if(user == null) {
					throw new RRException(s + "不存在");
				}
				return user.getUserId().intValue();
			}).collect(Collectors.toList());
			/*List<Integer> ids = new ArrayList<>();
			for(int i = 0; i < ss.length; i++) {
				SysUserEntity user = userService.queryByUserName(ss[i]);
				if(user == null) {
					throw new RRException(ss[i] + "不存在");
				}
				ids.add(user.getUserId().intValue());
			}*/
			taskItem.setEmployeeId(ids.get(0));
			taskItem.setEmployeeIds(StringUtils.join(ids, ","));
		}
	}
	
	/**
	 * 修改任务事项
	 */
	@SysLog("修改任务事项")
	@RequestMapping("/item/update")
	@RequiresPermissions("cal:task:update")
	public R updateTaskItem(CalTaskItem taskItem, Boolean updateTaskId){
		logger.info("修改任务事项,taskItem={}", JSON.toJSONString(taskItem));
		Integer userId = getUserId().intValue();
		
		//检查是否需要修改taskId
		logger.info("是否需要修改taskId={}", updateTaskId);
		if(updateTaskId != null && !updateTaskId) {
			taskItem.setTaskId(null);
		}
		//如果不是管理员,将状态修改为已完成,则提交审核
		if(userId > 1 && !sysUserService.hasAdminRole(userId.longValue()) && TaskStatusEnum.FINISHED.getCode() == taskItem.getStatus()) {
			//检查状态是否有修改
			CalTaskItem cti = taskItemService.queryObject(taskItem.getId());
			if(cti.getStatus().intValue() == taskItem.getStatus()) {
				logger.info("要修改的状态和数据库状态相同,status={}", cti.getStatus());
			}else {
				//检查是否有提交过
				Map<String, Object> params = new HashMap<>();
				params.put("taskItemId", taskItem.getId());
				params.put("targetStatus", TaskStatusEnum.FINISHED.getCode());
				params.put("applyUser", userId);
				List<CalTaskVerify> list = taskItemService.queryTaskVerifyList(params);
				if(CollectionUtils.isNotEmpty(list)) {
					throw new RRException("已提交过审核!");
				}
				CalTaskVerify ctv = new CalTaskVerify();
				ctv.setTaskItemId(taskItem.getId());
				ctv.setTargetStatus(taskItem.getStatus().byteValue());
				ctv.setApplyUser(userId);
				taskItemService.applyStatusChange(ctv);
			}
		}else {
			ValidatorUtils.validateEntity(taskItem, AddGroup.class);
			checkParam(taskItem);
			return taskItemService.update(taskItem);
		}
		return R.ok();
	}
	
	/**
	 * 修改任务事项状态
	 */
	@SysLog("修改任务状态")
	@RequestMapping("/updateItemStatus")
	@RequiresPermissions("cal:task:update")
	public R updateTaskItemStatus(CalTaskItem taskItem){
		return taskItemService.update(taskItem);
	}
	
	/**
	 * 获取日历事项
	 */
	@RequestMapping("/event/list/{status}")
	@RequiresPermissions("cal:task:event")
	public Object eventList(@PathVariable("status") Integer status, @RequestParam Map<String, Object> params){
		Integer userId = getUserId().intValue();
		//如果是超级管理员或者有管理员角色,则可以查看所有任务
		if(userId > 1 && !sysUserService.hasAdminRole(userId.longValue())) {
			params.put("employeeId", userId);
		}
		if(status != null) {
			params.put("status", status);
		}
		List<CalTaskItem> list = taskItemService.queryList(params);
//		return R.ok().put("page", list);
		return CalTaskItem.convertToEventObjectList(list);
	}
	
	/**
	 * 在日历中移动任务事项
	 */
	@SysLog("移动任务事项")
	@RequestMapping("/item/move")
	@RequiresPermissions("cal:task:update")
	public R moveTaskItem(Integer id, Integer days){
		CalTaskItem taskItem = new CalTaskItem();
		taskItem.setId(id);
		taskItemService.moveTaskItem(id, days);
		return R.ok();
	}
	
	/**
	 * 删除任务事项
	 */
	@SysLog("删除任务事项")
	@RequestMapping("/item/delete")
	@RequiresPermissions("cal:task:delete")
	public R deleteTaskItem(Integer id){
		taskItemService.delete(id);
		return R.ok();
	}
	
	//////////////////任务审批相关////////////////////////////////////////////////
	/**
	 * 任务待审核列表
	 */
	@RequestMapping("/verify_list")
	@RequiresPermissions("cal:task:list")
	public R verifyList(@RequestParam Map<String, Object> params){
		logger.info("查询任务待审核列表, params={}", params);
		//查询列表数据
		fuzzlyQuery(params);
		Query query = new Query(params);
		List<CalTaskVerify> taskList = taskItemService.queryTaskVerifyPage(params);
		int total = taskItemService.queryTaskVerifyTotal(query);
		
		PageUtils pageUtil = new PageUtils(taskList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 审批任务事项
	 */
	@SysLog("审批任务事项")
	@RequestMapping("/item/verify")
	@RequiresPermissions("cal:task:verify")
	public R verifyTaskItem(@RequestBody CalTaskVerify taskVerify){
		logger.info("审批任务事项,参数={}", JSON.toJSONString(taskVerify));
		Integer userId = getUserId().intValue();
		taskVerify.setVerifyUser(userId);
		int n = taskItemService.verifyTaskItem(taskVerify);
		return n > 0 ? R.ok() : R.error("审批失败");
	}
	
}

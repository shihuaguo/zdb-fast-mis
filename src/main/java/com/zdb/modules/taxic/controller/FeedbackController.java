package com.zdb.modules.taxic.controller;

import com.zdb.common.annotation.SysLog;
import com.zdb.common.utils.PageUtils;
import com.zdb.common.utils.Query;
import com.zdb.common.utils.R;
import com.zdb.common.validator.ValidatorUtils;
import com.zdb.common.validator.group.AddGroup;
import com.zdb.common.validator.group.UpdateGroup;
import com.zdb.modules.sys.controller.AbstractController;
import com.zdb.modules.taxic.entity.FeedbackInfo;
import com.zdb.modules.taxic.service.ITaxIcService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 信息反馈Controller
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2017年8月31日
 */
@RestController
@RequestMapping("/taxic/feedback")
@Slf4j
public class FeedbackController extends AbstractController {
	
	private final ITaxIcService service;

	@Autowired
	public FeedbackController(ITaxIcService service) {
		this.service = service;
	}

	protected void fuzzlyQuery(Map<String, Object> params) {
		String feedbackInfo = (String) params.get("feedbackInfo");
		if(StringUtils.isNotBlank(feedbackInfo)) {
			params.put("feedbackInfo", "%" + feedbackInfo + "%");
		}
	}
	
	/**
	 * 所有反馈信息列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("taxic:feedback:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils pageUtil = queryList(params);
		return R.ok().put("page", pageUtil);
	}
	
	protected PageUtils queryList(Map<String, Object> params){
		log.info("查询任务列表,params={}", params);
		//查询列表数据
		fuzzlyQuery(params);
		Query query = new Query(params);
		List<FeedbackInfo> taskList = service.queryList(query);
		int total = service.queryTotal(query);

		return new PageUtils(taskList, total, query.getLimit(), query.getPage());
	}
	
	/**
	 * 保存信息反馈
	 */
	@SysLog("保存信息反馈")
	@RequestMapping("/save")
	@RequiresPermissions("taxic:feedback:save")
	public R save(@RequestBody FeedbackInfo feedbackInfo){
		ValidatorUtils.validateEntity(feedbackInfo, AddGroup.class);
		long userId = getUserId();
		feedbackInfo.setCreateBy((int)userId);
		service.save(feedbackInfo);
		return R.ok();
	}
	
	/**
	 * 修改信息反馈
	 */
	@SysLog("修改信息反馈")
	@RequestMapping("/update")
	@RequiresPermissions("taxic:feedback:update")
	public R update(@RequestBody FeedbackInfo feedbackInfo){
		ValidatorUtils.validateEntity(feedbackInfo, UpdateGroup.class);
		long userId = getUserId();
		feedbackInfo.setUpdateBy((int)userId);
		int n = service.update(feedbackInfo);
		return n>=1 ? R.ok() : R.error(1, "更新失败");
	}
	
	/**
	 * 根据ID获取信息反馈
	 */
	@RequestMapping("/getById/{id}")
	@RequiresPermissions("taxic:feedback:list")
	public R info(@PathVariable("id") Integer id){
		FeedbackInfo feedbackInfo = service.queryObject(id);
		
		return R.ok().put("feedbackInfo", feedbackInfo);
	}
	
	/**
	 * 删除信息反馈
	 */
	@SysLog("删除信息反馈")
	@RequestMapping("/delete")
	@RequiresPermissions("taxic:feedback:delete")
	public R delete(@RequestBody Integer[] ids){
		service.deleteBatch(ids);
		return R.ok();
	}
}

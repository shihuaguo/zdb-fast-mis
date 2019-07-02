package com.zdb.modules.taxic.controller;

import com.zdb.common.annotation.SysLog;
import com.zdb.common.utils.PageUtils;
import com.zdb.common.utils.Query;
import com.zdb.common.utils.R;
import com.zdb.common.validator.ValidatorUtils;
import com.zdb.common.validator.group.AddGroup;
import com.zdb.common.validator.group.UpdateGroup;
import com.zdb.modules.sys.controller.AbstractController;
import com.zdb.modules.taxic.entity.QuestionWithBLOBs;
import com.zdb.modules.taxic.service.IQuestionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 问题管理Controller
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2018年12月3日
 */
@RestController
@RequestMapping("/taxic/question")
@Slf4j
public class QuestionController extends AbstractController {
	
	private final IQuestionService service;

	@Autowired
	public QuestionController(IQuestionService service) {
		this.service = service;
	}

	/**
	 * 所有反馈信息列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("taxic:question:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils pageUtil = queryList(params);
		return R.ok().put("page", pageUtil);
	}
	
	protected PageUtils queryList(Map<String, Object> params){
		log.info("查询问题列表,params={}", params);
		Query query = new Query(params);
		List<QuestionWithBLOBs> list = service.queryList(query);
		int total = service.queryTotal(query);
		return new PageUtils(list, total, query.getLimit(), query.getPage());
	}
	
	/**
	 * 保存问题
	 */
	@SysLog("保存问题")
	@RequestMapping("/save")
	@RequiresPermissions("taxic:question:save")
	public R save(@RequestBody QuestionWithBLOBs question){
		ValidatorUtils.validateEntity(question, AddGroup.class);
		long userId = getUserId();
		question.setCreateBy(String.valueOf(userId));
		service.save(question);
		return R.ok();
	}
	
	/**
	 * 修改问题
	 */
	@SysLog("修改问题")
	@RequestMapping("/update")
	@RequiresPermissions("taxic:question:update")
	public R update(@RequestBody QuestionWithBLOBs question){
		ValidatorUtils.validateEntity(question, UpdateGroup.class);
		int n = service.update(question);
		return n>=1 ? R.ok() : R.error(1, "更新失败");
	}
	
	/**
	 * 根据ID获取问题
	 */
	@RequestMapping("/getById/{id}")
	@RequiresPermissions("taxic:question:list")
	public R info(@PathVariable("id") Integer id){
		QuestionWithBLOBs question = service.queryObject(id);
		return R.ok().put("question", question);
	}
	
	/**
	 * 删除问题
	 */
	@SysLog("删除问题")
	@RequestMapping("/delete")
	@RequiresPermissions("taxic:question:delete")
	public R delete(@RequestBody Integer[] ids){
		service.deleteBatch(ids);
		return R.ok();
	}
}

package com.zdb.modules.customer.controller;

import com.alibaba.fastjson.JSON;
import com.zdb.common.annotation.SysLog;
import com.zdb.common.utils.*;
import com.zdb.common.validator.ValidatorUtils;
import com.zdb.common.validator.group.AddGroup;
import com.zdb.common.validator.group.UpdateGroup;
import com.zdb.modules.customer.entity.Customer;
import com.zdb.modules.customer.entity.CustomerIndustryCommerce;
import com.zdb.modules.customer.entity.CustomerTax;
import com.zdb.modules.customer.service.ICustomerIcService;
import com.zdb.modules.customer.service.ICustomerService;
import com.zdb.modules.customer.service.ICustomerTaxService;
import com.zdb.modules.sys.controller.AbstractController;
import com.zdb.modules.sys.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/customer")
@Slf4j
public class CustomerController extends AbstractController {
	
	private final ICustomerService customerService;
	
	private final ICustomerIcService customerIcService;
	private final ICustomerTaxService customerTaxService;
	
	private final SysUserService userService;

	@Autowired
	public CustomerController(ICustomerService customerService, ICustomerIcService customerIcService, ICustomerTaxService customerTaxService, SysUserService userService) {
		this.customerService = customerService;
		this.customerIcService = customerIcService;
		this.customerTaxService = customerTaxService;
		this.userService = userService;
	}

	protected void fuzzlyQuery(Map<String, Object> params) {
		String customerName = (String) params.get("customerName");
		if(StringUtils.isNotBlank(customerName)) {
			params.put("customerName", "%" + customerName + "%");
		}
		String customerNameOrNo = (String) params.get("customerNameOrNo");
		if(StringUtils.isNotBlank(customerNameOrNo)) {
			params.put("customerNameOrNo", "%" + customerNameOrNo + "%");
		}
	}

	/**
	 * 客户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("customer:list")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils pageUtil = queryList(params);
		return R.ok().put("page", pageUtil);
	}
	
	protected PageUtils queryList(Map<String, Object> params){
		//如果不是管理员,则只能查看有限的客户
		Long userId = getUserId();
		if(userId != Constant.SUPER_ADMIN && !userService.hasAdminRole(userId)) {
			params.put("filterByUser", "1");
			params.put("userId", userId);
		}
		//查询列表数据
		log.info("查询客户列表,参数={}", params);
		fuzzlyQuery(params);
		Query query = new Query(params);
		List<Customer> customerList = customerService.queryListWithIcTax(query);
		int total = customerService.queryTotal(query);
		return new PageUtils(customerList, total, query.getLimit(), query.getPage());
	}
	
	/**
	 * 导出到Excel
	 * @param params page query params
	 * @param res	 http servlet response
	 */
	@RequestMapping("/export")
	@RequiresPermissions("customer:save")
	public void export(@RequestParam Map<String, Object> params, HttpServletRequest req, HttpServletResponse res) {
		String fileName = "客户信息导出_" + DateUtils.format(new Date(), "yyyy-MM-dd") + ".xls";
		//如果不是管理员,则只能查看有限的客户
		Long userId = getUserId();
		if(userId != Constant.SUPER_ADMIN && !userService.hasAdminRole(userId)) {
			params.put("filterByUser", "1");
			params.put("userId", userId);
		}
		//查询列表数据
		log.info("导出客户列表,参数={}", params);
		fuzzlyQuery(params);
		Query query = new Query(params);
		List<Customer> customerList = customerService.queryListWithIcTax(query);
		export(customerList, Customer.class, fileName, req, res);
	}
	
	/**
	 * 获取客户的check_login_state
	 */
	@RequestMapping("/checkLoginState")
	@RequiresPermissions("customer:list")
	public R queryCheckLoginState(Integer customerId){
		//查询列表数据
		log.info("获取客户的check_login_state,参数={}", customerId);
		String checkLoginState = customerService.queryCheckLoginStateByPrimaryKey(customerId);
		
		return R.ok().put("data", checkLoginState);
	}
	
	/**
	 * 客户名称列表
	 */
	@RequestMapping("/namelist")
	@RequiresPermissions("customer:list")
	public Object nameList(@RequestParam Map<String, Object> params){
		log.info("查询客户名称列表,参数={}", params);
		//查询列表数据
		String term = params.get("term").toString();
		if(term != null) {
			params.put("term", "%" + term + "%");
		}
		return customerService.queryNameList(params);
	}
	
	/**
	 * 客户列表(包含工商信息)
	 */
	@RequestMapping("/listWithIc")
	@RequiresPermissions("customer:list")
	public R listWithIc(@RequestParam Map<String, Object> params){
		//如果不是管理员,则只能查看有限的客户
		Long userId = getUserId();
		if(userId != Constant.SUPER_ADMIN && !userService.hasAdminRole(userId)) {
			params.put("filterByUser", "1");
			params.put("userId", userId);
		}
		log.info("查询客户工商信息列表,参数={}", params);
		//查询列表数据
		fuzzlyQuery(params);
		//过滤掉已删除客户
		params.put("status", 0);
		Query query = new Query(params);
		List<Customer> customerList = customerService.queryListWithIc(query);
		int total = customerService.queryTotalWithIc(query);
		
		PageUtils pageUtil = new PageUtils(customerList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 客户列表(包含税务信息)
	 */
	@RequestMapping("/listWithTax")
	@RequiresPermissions("customer:list")
	public R listWithTax(@RequestParam Map<String, Object> params){
		//如果不是管理员,则只能查看有限的客户
		Long userId = getUserId();
		if(userId != Constant.SUPER_ADMIN && !userService.hasAdminRole(userId)) {
			params.put("filterByUser", "1");
			params.put("userId", userId);
		}
		log.info("查询客户税务信息列表,参数={}", params);
		//查询列表数据
		fuzzlyQuery(params);
		//过滤掉已删除客户
		params.put("status", 0);
		Query query = new Query(params);
		List<Customer> customerList = customerService.queryListWithTax(query);
		int total = customerService.queryTotalWithTax(query);
		
		PageUtils pageUtil = new PageUtils(customerList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 保存客户工商登记信息
	 */
	@SysLog("保存客户工商登记信息")
	@RequestMapping("/saveIc")
	@RequiresPermissions("customer:save")
	public R saveIc(@RequestBody Customer customer){
		log.info("保存客户工商登记信息,参数={}", JSON.toJSONString(customer));

		ValidatorUtils.validateEntity(customer, AddGroup.class);
		//检查编号是否存在
		if(StringUtils.isNotBlank(customer.getCustomerNo())) {
			Map<String, Object> map = new HashMap<>();
			map.put("customerNo", customer.getCustomerNo());
			//过滤掉已删除客户
			map.put("status", 0);
			List<Customer> list = customerService.queryList(map);
			if(list != null && !list.isEmpty()) {
				return R.error("编号" + customer.getCustomerNo() + "已存在");
			}
		}
		customerService.save(customer);
		return R.ok();
	}
	
	/**
	 * 保存客户税务登记信息
	 */
	@SysLog("保存客户税务登记信息")
	@RequestMapping("/saveTax")
	@RequiresPermissions("customer:save")
	public R saveTax(@RequestBody Customer customer){
		log.info("保存客户税务登记信息,参数={}", JSON.toJSONString(customer));

		ValidatorUtils.validateEntity(customer, AddGroup.class);
		customerService.save(customer);
		return R.ok();
	}
	
	/**
	 * 根据ID获取客户
	 */
	@RequestMapping("/getById/{id}")
//	@RequiresPermissions("cal:task:getById")
	public R info(@PathVariable("id") Integer id){
		Customer customer = customerService.queryObject(id);
		CustomerIndustryCommerce customerIc = customerIcService.queryObject(id);
		CustomerTax customerTax = customerTaxService.queryObject(id);
		return R.ok().put("customer", customer).put("customerIc", customerIc).put("customerTax", customerTax);
	}
	
	/**
	 * 删除客户
	 */
	@SysLog("删除客户")
	@RequestMapping("/delete")
	@RequiresPermissions("customer:delete")
	public R delete(@RequestBody Integer[] ids){
		log.info("删除客户,参数={}", StringUtils.join(ids, ","));
		customerService.deleteBatch(ids);
		return R.ok();
	}
	
	/**
	 * 修改客户
	 */
	@SysLog("修改客户")
	@RequestMapping("/update")
	@RequiresPermissions("customer:update")
	public R update(@RequestBody Customer customer){
		log.info("修改客户信息,参数={}", JSON.toJSONString(customer));
		ValidatorUtils.validateEntity(customer, UpdateGroup.class);
		//检查编号是否存在
		if(StringUtils.isNotBlank(customer.getCustomerNo())) {
			Map<String, Object> map = new HashMap<>();
			map.put("customerNo", customer.getCustomerNo());
			map.put("id", customer.getId());
			//过滤掉已删除客户
			map.put("status", 0);
			List<Customer> list = customerService.queryByCustomerNo(map);
			if(list != null && !list.isEmpty()) {
				return R.error("编号" + customer.getCustomerNo() + "已存在");
			}
		}
		int n = customerService.update(customer);
		return n>=1 ? R.ok() : R.error(1, "更新失败");
	}
	
	//+ vm.customerIc.socialReditOde + "code=" + validCode;
	private HttpClientUtilKA kd = new HttpClientUtilKA();
	
	@RequestMapping(value = "/validCode/indAndCom", method = RequestMethod.GET)
	public void getICValidCode(HttpServletResponse res, String random) {
		log.info("请求工商验证码, random={}", random);
		String fileName = "valideIcCode.jpg";
		res.setHeader("content-type", "image/png");
		res.setContentType("image/png");
		res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		String fileUrl = kd.doDownload(GZpublicityUtil.URL_validecodeof_ic + random, null);
		if(fileUrl != null) {
			log.info("将验证码图片从广州红盾网拉取到服务器成功");
			try(InputStream is = new FileInputStream(fileUrl)){
				IOUtils.copy(is, res.getOutputStream());
			} catch (IOException e) {
				log.error("将验证码图片从广州红盾信息网拉取并发送到网页失败", e);
			}
		}else {
			log.error("从广州红盾信息网获取验证码失败,url={}", GZpublicityUtil.URL_validecodeof_ic + random);
		}
	}
	
	/**
	 * 从广州市工商行政管理局同步工商信息
	 * @return r
	 */
	@RequiresPermissions("customer:update")
	@RequestMapping("/sync/ic")
	public R syncIndAndComInfo(String socialReditOde, String validCode) {
		log.info("收到同步工商信息请求, socialReditOde={}, valideCode={}", socialReditOde, validCode);
		return GZpublicityUtil.syncIndAndComInfo(socialReditOde, validCode, kd);
	}
	
	@RequestMapping(value = "/validCode/tax", method = RequestMethod.GET)
	public void getTaxValidCode(HttpServletResponse res, String random) {
		log.info("请求税局验证码图片, random={}", random);
		String fileName = "valideIcCode.jpg";
		res.setHeader("content-type", "image/png");
		res.setContentType("image/png");
		res.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		String fileUrl = kd.doDownload(EtaxUtil.URL_checkcode + random, null);
		if(fileUrl != null) {
			log.info("将验证码图片从税局拉取到服务器成功");
			try(InputStream is = new FileInputStream(fileUrl)){
				IOUtils.copy(is, res.getOutputStream());
			} catch (IOException e) {
				log.error("将验证码图片从税局拉取并发送到网页失败", e);
			}
		}else {
			log.error("从税局获取验证码失败,url={}", EtaxUtil.URL_checkcode + random);
		}
	}
	
	/**
	 * 从税局同步税务信息
	 * @return r
	 */
	@RequiresPermissions("customer:update")
	@RequestMapping("/sync/tax")
	public R syncTaxInfo( String legalPersonAccount, String legalPersonPassword, String customerName, String validCode,String csessionid ,String sig , String token ,String scene, String customerId) {
		log.info("收到同步税务信息请求, customerId={},legalPersonAccount={}, legalPersonPassword={}, customerName={}, valideCode={},csessionid={},sig={},token={},scene={}",
				customerId, legalPersonAccount, legalPersonPassword, customerName, validCode,csessionid,sig,token,scene);
		return EtaxUtil.syncTaxInfo(customerName, legalPersonAccount, legalPersonPassword, validCode,csessionid,sig,token,scene, kd);
	}
	
	///////////////////////////////////////////商事信息/////////////////////////////////////
	@RequestMapping(value = "/validCode/cri", method = RequestMethod.GET)
	public R getCriValidCode(HttpServletResponse res, String random) {
		log.info("请求商事主体信息平台验证码图片, random={}", random);
		return CriUtil.getValidateCodeUrl(kd);
	}
	
	/**
	 * 从商事主体信息平台同步信息
	 * @return r
	 */
	@RequiresPermissions("customer:update")
	@RequestMapping("/sync/cri")
	public R syncCri(String socialReditOde, String validCode, String guid) {
		log.info("收到同步商事主体信息请求, socialReditOde={},validCode={}, guid={}",
				socialReditOde, validCode, guid);
		return CriUtil.syncCri(socialReditOde, validCode, guid, kd);
	}
	
	/**
	 * 保存商事主体信息
	 */
	@SysLog("保存商事主体信息")
	@RequestMapping("/updateCri")
	@RequiresPermissions("customer:update")
	public R saveCri(String businessStatus, String AbnormalList, Integer customerId){
		log.info("保存商事主体信息,businessStatus={},AbnormalList={},customerId={}", businessStatus, AbnormalList, customerId);

		CustomerIndustryCommerce ic = new CustomerIndustryCommerce();
		ic.setCustomerId(customerId);
		ic.setBusinessStatus(businessStatus);
		ic.setAbnormalList(AbnormalList);
		customerIcService.update(ic);
		return R.ok();
	}
	
}

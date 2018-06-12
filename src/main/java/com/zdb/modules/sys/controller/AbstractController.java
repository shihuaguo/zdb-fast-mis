package com.zdb.modules.sys.controller;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import com.zdb.common.utils.FilenameUtil;
import com.zdb.common.utils.PageUtils;
import com.zdb.common.utils.excel.ExcelUtil;
import com.zdb.common.utils.excel.ReflectionUtil;
import com.zdb.modules.sys.entity.SysUserEntity;

/**
 * Controller公共组件
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月9日 下午9:42:26
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SysUserEntity getUser() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}
	
	protected void fuzzlyQuery(Map<String, Object> params) {
		
	}
	
	/**
	 * 根据参数查询数据
	 * @param params
	 * @return
	 */
	protected PageUtils queryList(Map<String, Object> params) {
		logger.info("default queryList, subclasss should implement it");
		return null;
	};
	
	/**
	 * 数据导出
	 * @param params
	 * @param req
	 * @param res
	 */
	protected void export(@RequestParam Map<String, Object> params, Class<?> clazz, String fileName, HttpServletRequest req, HttpServletResponse res) {
		PageUtils pu = queryList(params);
		List<?> list = pu.getList();
		export(list, clazz, fileName, req, res);
	}
	
	protected void export(List<?> list, Class<?> clazz, String fileName, HttpServletRequest req, HttpServletResponse res) {
		logger.info("导出{}信息,count={}", clazz.getName(), list.size());
		Map<String, String> headers = ReflectionUtil.getFieldAndCNames(clazz);
		headers.putAll(ReflectionUtil.getMethodAndCNames(clazz));
		res.setContentType("multipart/form-data");
		res.setHeader("Content-Disposition", "attachment;fileName=" + FilenameUtil.filename(fileName, req));
		try(OutputStream os = res.getOutputStream()){
			ExcelUtil.exportExcel(headers , list, os);
		}catch (Exception e) {
			logger.error("导出{}信息异常:", fileName, e);
		}
	}
	
}

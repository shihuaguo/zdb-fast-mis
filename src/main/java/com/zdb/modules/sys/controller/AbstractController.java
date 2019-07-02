package com.zdb.modules.sys.controller;

import com.zdb.common.utils.FilenameUtil;
import com.zdb.common.utils.PageUtils;
import com.zdb.common.utils.excel.ExcelUtil;
import com.zdb.common.utils.excel.ReflectionUtil;
import com.zdb.modules.sys.entity.SysUserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * Controller公共组件
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月9日 下午9:42:26
 */
@Slf4j
public abstract class AbstractController {
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
     *
     * @param params params
     * @return page
     */
    protected PageUtils queryList(Map<String, Object> params) {
        log.info("default queryList, subclasss should implement it");
        return null;
    }

    /**
     * 数据导出
     */
    protected void export(@RequestParam Map<String, Object> params, Class<?> clazz, String fileName, HttpServletRequest req, HttpServletResponse res) {
        PageUtils pu = queryList(params);
        List<?> list = pu.getList();
        export(list, clazz, fileName, req, res);
    }

    protected void export(List<?> list, Class<?> clazz, String fileName, HttpServletRequest req, HttpServletResponse res) {
        log.info("导出{}信息,count={}", clazz.getName(), list.size());
        Map<String, String> headers = ReflectionUtil.getFieldAndCNames(clazz);
        headers.putAll(ReflectionUtil.getMethodAndCNames(clazz));
        res.setContentType("multipart/form-data");
        res.setHeader("Content-Disposition", "attachment;fileName=" + FilenameUtil.filename(fileName, req));
        try (OutputStream os = res.getOutputStream()) {
            ExcelUtil.exportExcel(headers, list, os);
        } catch (Exception e) {
            log.error("导出{}信息异常:", fileName, e);
        }
    }

}

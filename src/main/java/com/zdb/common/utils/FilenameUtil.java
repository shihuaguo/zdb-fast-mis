package com.zdb.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 处理不同浏览器导出文件时,文件名称乱码的问题
 * 〈功能详细描述〉
 * @author shihuaguo
 * @date 2016年12月8日
 */
public class FilenameUtil {
	private static final Logger logger = LoggerFactory.getLogger(FilenameUtil.class);

	public static String filename(String originalFileName, HttpServletRequest request){
		String newFileName;
		//firefox浏览器
		try {
			if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0){
				newFileName = new String(originalFileName.getBytes("UTF-8"), "ISO8859-1");
			}else {
				newFileName = URLEncoder.encode(originalFileName, "UTF-8");//IE浏览器
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("转换文件名称失败: ", e);
			newFileName = originalFileName;
		}
		return newFileName;
	}
}

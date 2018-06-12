package com.zdb.modules.ueditor;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.zdb.common.utils.RandomUtils;

/**
 * 提供处理ueditor的一些实用方法
 * 
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2018年2月22日
 */
public class UeditorUtil {
	
	public static final String UEDITOR_PATH_PATTERN_STR = "\\{(time|yyyy|yy|mm|dd|hh|ii|ss|rand\\:\\d)\\}";
	
	public static final Pattern UEDITOR_PATH_PATTERN = Pattern.compile(UEDITOR_PATH_PATTERN_STR);

	/**
	 * 解析文件路径(ueditor的路径)
	 * {rand:6} 会替换成随机数,后面的数字是随机数的位数 
     * {time} 会替换成时间戳 
     * {yyyy} 会替换成四位年份 
     * {yy} 会替换成两位年份 
     * {mm} 会替换成两位月份 
     * {dd} 会替换成两位日期 
     * {hh} 会替换成两位小时 
     * {ii} 会替换成两位分钟 
     * {ss} 会替换成两位秒 
	 * @param imagePathFormat
	 * @return
	 */
	public static String parsePathFormat(String imagePathFormat) {
		StringBuilder sb = new StringBuilder();
		Date date = new Date();
		Matcher matcher = UEDITOR_PATH_PATTERN.matcher(imagePathFormat);
		int pend = 0, start = 0, end = 0;
		while(matcher.find(start)) {
			start = matcher.start();
			end = matcher.end();
			if(start > pend) {
				sb.append(imagePathFormat.substring(pend, start));
			}
			String group = imagePathFormat.substring(start, end);
			if("{yyyy}".equals(group)) {
				sb.append(DateFormatUtils.format(date, "yyyy"));
			}else if("{yy}".equals(group)) {
				sb.append(DateFormatUtils.format(date, "yy"));
			}else if("{mm}".equals(group)) {
				sb.append(DateFormatUtils.format(date, "MM"));
			}else if("{dd}".equals(group)) {
				sb.append(DateFormatUtils.format(date, "dd"));
			}else if("{hh}".equals(group)) {
				sb.append(DateFormatUtils.format(date, "hh"));
			}else if("{ii}".equals(group)) {
				sb.append(DateFormatUtils.format(date, "ii"));
			}else if("{ss}".equals(group)) {
				sb.append(DateFormatUtils.format(date, "ss"));
			}else if("{time}".equals(group)) {
				sb.append(date.getTime());
			}else {
				//System.out.println(group);
				int rand = Integer.valueOf(group.substring(6, group.length()-1));
				sb.append(RandomUtils.randomNum(rand));
			}
			//System.out.println("start=" + start + ",group=" + group);
			start = end;
			pend = end;
		}
		if(pend < imagePathFormat.length()) {
			sb.append(imagePathFormat.substring(pend, imagePathFormat.length()));
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		String str = "/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}";
		Matcher matcher = UEDITOR_PATH_PATTERN.matcher(str);
		int index = 0;
		while(matcher.find(index)) {
			int start = matcher.start();
			int end = matcher.end();
			String group = str.substring(start, end);
			System.out.println("start=" + start + ",group=" + group);
			index = end;
		}
		
		System.out.println(parsePathFormat(str));
	}
}

package com.zdb.common.utils.excel;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 对于field上面使用了ExcelCell注解的JavaBean,通过反射机制获取
 * fieldname和中文名称,用于Excel导出
 * 
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2018年2月1日
 */
public class ReflectionUtil {
	/**
     * 获取field和对应的中文名称,用于Excel导出
     * @return
     */
    public static Map<String, String> getFieldAndCNames(Class<?> clazz){
    	Map<String, String> map = new LinkedHashMap<>();
    	Field[] fields = clazz.getDeclaredFields();
    	List<Field> flist = new ArrayList<>();
    	
    	Map<String, String> submap = new LinkedHashMap<>();
    	for(Field f : fields) {
    		Class<?> clazz1 = f.getType();
    		//如果field是一个Exportable,递归解析
    		if(Exportable.class.isAssignableFrom(clazz1)) {
    			submap.putAll(getFieldAndCNames(clazz1));
    		}else {
    			ExcelCell ec = f.getAnnotation(ExcelCell.class);
    			if(ec != null) {
    				flist.add(f);
    			}
    		}
    	}
    	flist.stream().sorted((f1,f2)->{
    		return f1.getAnnotation(ExcelCell.class).index() - f2.getAnnotation(ExcelCell.class).index();
    	}).forEach((f) -> {
    		map.put(f.getName(), f.getAnnotation(ExcelCell.class).cname());
    	});
    	
    	map.putAll(submap);
    	
    	return map;
    }
    
    public static Map<String, String> getMethodAndCNames(Class<?> clazz){
    	Map<String, String> map = new LinkedHashMap<>();
    	//方法
    	Method[] methods = clazz.getDeclaredMethods();
    	List<Method> mlist = new ArrayList<>();
    	for(Method m : methods) {
    		ExcelCell ec = m.getAnnotation(ExcelCell.class);
    		if(ec != null) {
    			mlist.add(m);
    		}
    	}
    	mlist.stream().sorted((f1,f2) -> {
    		return f1.getAnnotation(ExcelCell.class).index() - f2.getAnnotation(ExcelCell.class).index();
    	}).forEach((f) -> {
    		map.put(f.getName(), f.getAnnotation(ExcelCell.class).cname());
    	});
    	return map;
    }
}

package com.zdb.modules.cal.service;

import java.util.List;
import java.util.Map;

import com.zdb.common.utils.R;
import com.zdb.modules.cal.entity.CalTaskItem;
import com.zdb.modules.cal.entity.CalTaskVerify;

/**
 * 新建任务
 * 
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2017年8月31日
 */
public interface ICalTaskItemService {
	
	CalTaskItem queryObject(Integer id);
	
	void save(CalTaskItem calTaskItem);
	
	R update(CalTaskItem calTaskItem);
	
	int delete(Integer id);
	
	//根据任务ID查询事项
    List<CalTaskItem> queryByTaskId(Integer taskId);
    
    //删除任务时,删除对应的事项
    int deleteByTaskId(Integer taskId);
    
    /**
     * 根据参数查询,可能的参数包括
     * taskId:任务ID
     * employeeId:员工ID
     * start: 开始时间
     * end: 结束时间
     * @param params
     * @return
     */
    List<CalTaskItem> queryList(Map<String, Object> params);
    
    int queryTotal(Map<String, Object> map);
    
    /**
     * 移动任务
     * @param id
     * @param days
     * @return
     */
    int moveTaskItem(Integer id, Integer days);
    
    /**
     * 申请变更任务事项状态
     * @param ctv
     * @return
     */
    int applyStatusChange(CalTaskVerify ctv);
    
    List<CalTaskVerify> queryTaskVerifyList(Map<String, Object> params);
    
    List<CalTaskVerify> queryTaskVerifyPage(Map<String, Object> params);
    
    int queryTaskVerifyTotal(Map<String, Object> map);
    
    /**
     * 审批任务事项
     * @param ctv
     * @return
     */
    int verifyTaskItem(CalTaskVerify ctv);
    
}

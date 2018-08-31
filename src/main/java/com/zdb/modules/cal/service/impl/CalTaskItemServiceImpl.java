package com.zdb.modules.cal.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdb.common.exception.RRException;
import com.zdb.common.utils.R;
import com.zdb.modules.cal.dao.CalTaskItemMapper;
import com.zdb.modules.cal.dao.CalTaskMapper;
import com.zdb.modules.cal.dao.CalTaskVerifyMapper;
import com.zdb.modules.cal.entity.CalTask;
import com.zdb.modules.cal.entity.CalTaskItem;
import com.zdb.modules.cal.entity.CalTaskVerify;
import com.zdb.modules.cal.entity.TaskStatusEnum;
import com.zdb.modules.cal.service.ICalTaskItemService;

@Service("calTaskItemService")
public class CalTaskItemServiceImpl implements ICalTaskItemService {
	
	private static final Logger logger = LoggerFactory.getLogger(CalTaskItemServiceImpl.class);
	
	@Autowired
	private CalTaskItemMapper mapper;
	
	@Autowired
	private CalTaskMapper taskMapper;
	
	@Autowired
	private CalTaskVerifyMapper verifyMapper;
	
	@Override
	public CalTaskItem queryObject(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public void save(CalTaskItem calTaskItem) {
		mapper.insertSelective(calTaskItem);
	}

	/**
	 * 如果修改事项状态完成，判断整个任务是否完成
	 * 如果修改事项未完成，则修改任务为完成
	 */
	@Override
	@Transactional
	public R update(CalTaskItem calTaskItem) {
		R r = R.ok();
		CalTaskItem itemDb = mapper.selectByPrimaryKey(calTaskItem.getId());
		Integer statusDb = itemDb.getStatus();
		calTaskItem.setVersion(itemDb.getVersion());
		int n = mapper.updateByPrimaryKeySelective(calTaskItem);
		if(n > 0) {
			Integer taskId = itemDb.getTaskId();
			//检查是否修改了taskId
			if(calTaskItem.getTaskId() != null && calTaskItem.getTaskId() != taskId.intValue()) {
				r.put("msg", "修改了任务的taskId");
			}
			//仅当修改事项的状态,且状态和数据库不一致时
			if(taskId != null && calTaskItem.getStatus() != null && calTaskItem.getStatus() != statusDb.intValue()) {
				//是否需要联动修改任务的状态
				boolean updateTask = true;
				//联动修改任务的状态
				int taskStatus = 0;
				if(calTaskItem.getStatus() == 1) {
					logger.info("修改事项为完成状态,判断任务是否完成");
					List<CalTaskItem> itemList = mapper.queryByTaskId(taskId);
					if(CollectionUtils.isNotEmpty(itemList)) {
						for(CalTaskItem item : itemList) {
							if(!Integer.valueOf(1).equals(item.getStatus())) {
								updateTask = false;
							}
						}
					}
					if(updateTask) {
						logger.info("任务{}的所有事项都已完成,需要修改任务状态为完成状态", taskId);
						taskStatus = 1;
					}
				}else {
					logger.info("事项{}修改为未完成状态,修改对应的任务{}为未完成状态", itemDb.getId(), taskId);
				}
				if(updateTask) {
					CalTask task = new CalTask();
					task.setId(taskId);
					task.setStatus((byte) taskStatus);
					taskMapper.updateByPrimaryKeySelective(task);
				}
				r.put("taskStatus", taskStatus);
			}
		}
		return r;
	}

	@Override
	public int delete(Integer id) {
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<CalTaskItem> queryByTaskId(Integer taskId) {
		return mapper.queryByTaskId(taskId);
	}

	@Override
	public int deleteByTaskId(Integer taskId) {
		return mapper.deleteByTaskId(taskId);
	}

	@Override
	public List<CalTaskItem> queryList(Map<String, Object> params) {
		return mapper.queryList(params);
	}

	@Override
	public int moveTaskItem(Integer id, Integer days) {
		Map<String, Object> params = new HashMap<>();
		params.put("id", id);
		params.put("days", days);
		return mapper.moveTaskItem(params);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return mapper.queryTotal(map);
	}

	@Override
	public int applyStatusChange(CalTaskVerify ctv) {
		return verifyMapper.insertSelective(ctv);
	}

	@Override
	public List<CalTaskVerify> queryTaskVerifyList(Map<String, Object> params) {
		return verifyMapper.queryList(params);
	}

	@Override
	public List<CalTaskVerify> queryTaskVerifyPage(Map<String, Object> params) {
		return verifyMapper.queryPage(params);
	}

	@Override
	public int queryTaskVerifyTotal(Map<String, Object> map) {
		return verifyMapper.queryTotal(map);
	}

	@Override
	@Transactional
	public int verifyTaskItem(CalTaskVerify ctv) {
		CalTaskVerify ctvDb = verifyMapper.selectByPrimaryKey(ctv.getId());
		Assert.assertNotNull(ctv.getVerifyStatus());
		if(ctv.getVerifyStatus().intValue() == ctvDb.getVerifyStatus().intValue()) {
			throw new RRException("状态错误");
		}
//		if(ctvDb.getVerifyStatus().intValue() != TaskStatusEnum.UN_FINISHED.getCode()) {
//			throw new RRException("任务事项已经审批");
//		}
		ctv.setVersion(ctvDb.getVersion());
		int n = verifyMapper.updateByPrimaryKeySelective(ctv);
		if(n > 0) {
			CalTaskItem cti = new CalTaskItem();
			cti.setId(ctv.getTaskItemId());
			TaskStatusEnum tse = ctv.getVerifyStatus().intValue() == TaskStatusEnum.FINISHED.getCode() ? TaskStatusEnum.FINISHED : TaskStatusEnum.UN_FINISHED;
			cti.setStatus(tse.getCode());
			n = mapper.updateByPrimaryKeySelective(cti);
		}else {
			logger.info("修改审批表返回0,可能状态已被修改");
		}
		return n;
	}

}

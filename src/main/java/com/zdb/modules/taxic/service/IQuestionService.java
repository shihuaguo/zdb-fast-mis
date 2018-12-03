package com.zdb.modules.taxic.service;

import com.zdb.modules.taxic.entity.QuestionWithBLOBs;

import java.util.List;
import java.util.Map;

public interface IQuestionService {
    /**
     * 查询单个问题,当修改问题的时候,需要先查出该问题的内容
     * @param id 问题的id
     * @return question
     */
    QuestionWithBLOBs queryObject(Integer id);
    /**
     * 查询问题列表,在查询页面调用该方法
     * @param map 查询参数
     * @return list of question
     */
    List<QuestionWithBLOBs> queryList(Map<String, Object> map);
    /**
     * 查询问题数量,在查询页面调用该方法
     * @param map 查询参数
     * @return count of question
     */
    int queryTotal(Map<String, Object> map);
    /**
     * 保存问题
     * @param question question to be saved
     */
    int save(QuestionWithBLOBs question);
    /**
     * 修改问题
     * @param question question be to update
     * @return count of question
     */
    int update(QuestionWithBLOBs question);
    /**
     * 删除单个问题
     * @param id
     */
    void delete(Integer id);
    /**
     * 删除多个问题
     * @param ids
     */
    void deleteBatch(Integer[] ids);
}

package com.zdb.modules.taxic.service.impl;

import com.zdb.modules.taxic.dao.QuestionMapper;
import com.zdb.modules.taxic.entity.Question;
import com.zdb.modules.taxic.entity.QuestionWithBLOBs;
import com.zdb.modules.taxic.service.IQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author shihuaguo
 * @email huaguoshi@gmail.com
 * @date 2018-12-03
 **/
@Service("questionService")
public class QuestionServiceImpl implements IQuestionService {

    private final QuestionMapper mapper;

    @Autowired
    public QuestionServiceImpl(QuestionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public QuestionWithBLOBs queryObject(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    @Override
    public List<QuestionWithBLOBs> queryList(Map<String, Object> map) {
        return mapper.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return mapper.queryTotal(map);
    }

    @Override
    public int save(QuestionWithBLOBs question) {
        return mapper.insertSelective(question);
    }

    @Override
    public int update(QuestionWithBLOBs question) {
        return mapper.updateByPrimaryKeySelective(question);
    }

    @Override
    public void delete(Integer id) {
        mapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteBatch(Integer[] ids) {

    }
}

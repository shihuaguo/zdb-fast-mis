package com.zdb.modules.taxic.dao;

import com.zdb.modules.taxic.entity.Question;
import com.zdb.modules.taxic.entity.QuestionWithBLOBs;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuestionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuestionWithBLOBs record);

    int insertSelective(QuestionWithBLOBs record);

    QuestionWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuestionWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(QuestionWithBLOBs record);

    int updateByPrimaryKey(Question record);

    List<QuestionWithBLOBs> queryList(Map<String, Object> map);

    int queryTotal(Map<String, Object> map);
}
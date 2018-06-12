package com.zdb.modules.ueditor;

import org.apache.ibatis.annotations.Mapper;

import com.zdb.modules.ueditor.RichTextInfo;

@Mapper
public interface RichTextInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RichTextInfo record);

    int insertSelective(RichTextInfo record);

    RichTextInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RichTextInfo record);

    int updateByPrimaryKeyWithBLOBs(RichTextInfo record);

    int updateByPrimaryKey(RichTextInfo record);
    
    RichTextInfo queryByType(RichTextInfo record);
}
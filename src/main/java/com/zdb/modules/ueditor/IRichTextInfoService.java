package com.zdb.modules.ueditor;

public interface IRichTextInfoService {

	int deleteByPrimaryKey(Integer id);

    int insertSelective(RichTextInfo record);

    RichTextInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RichTextInfo record);

    int updateByPrimaryKeyWithBLOBs(RichTextInfo record);

    RichTextInfo queryByType(RichTextInfo record);
}

package com.zdb.modules.ueditor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("richTextInfoService")
public class RichTextInfoServiceImpl implements IRichTextInfoService {
	
	//private static final Logger logger = LoggerFactory.getLogger(RichTextInfoServiceImpl.class);
	
	@Autowired
	private RichTextInfoMapper mapper;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return mapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(RichTextInfo record) {
		return mapper.insertSelective(record);
	}

	@Override
	public RichTextInfo selectByPrimaryKey(Integer id) {
		return mapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(RichTextInfo record) {
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(RichTextInfo record) {
		return mapper.updateByPrimaryKeyWithBLOBs(record);
	}

	@Override
	public RichTextInfo queryByType(RichTextInfo record) {
		return mapper.queryByType(record);
	}

}

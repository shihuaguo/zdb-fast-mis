package com.zdb.modules.crawl.service.impl;

import com.zdb.modules.crawl.entity.CrawlCompanyInfo;
import com.zdb.modules.crawl.dao.CrawlCompanyInfoDao;
import com.zdb.modules.crawl.service.CrawlCompanyInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (CrawlCompanyInfo)表服务实现类
 *
 * @author makejava
 * @since 2020-04-23 23:02:37
 */
@Service("crawlCompanyInfoService")
public class CrawlCompanyInfoServiceImpl implements CrawlCompanyInfoService {
    @Resource
    private CrawlCompanyInfoDao crawlCompanyInfoDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public CrawlCompanyInfo queryById(Long id) {
        return this.crawlCompanyInfoDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    @Override
    public List<CrawlCompanyInfo> queryAllByLimit(int offset, int limit) {
        return this.crawlCompanyInfoDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param crawlCompanyInfo 实例对象
     * @return 实例对象
     */
    @Override
    public CrawlCompanyInfo insert(CrawlCompanyInfo crawlCompanyInfo) {
        this.crawlCompanyInfoDao.insert(crawlCompanyInfo);
        return crawlCompanyInfo;
    }

    /**
     * 修改数据
     *
     * @param crawlCompanyInfo 实例对象
     * @return 实例对象
     */
    @Override
    public CrawlCompanyInfo update(CrawlCompanyInfo crawlCompanyInfo) {
        this.crawlCompanyInfoDao.update(crawlCompanyInfo);
        return this.queryById(crawlCompanyInfo.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.crawlCompanyInfoDao.deleteById(id) > 0;
    }
}
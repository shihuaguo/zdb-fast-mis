package com.zdb.modules.crawl.dao;

import com.zdb.modules.crawl.entity.CrawlCompanyInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (CrawlCompanyInfo)表数据库访问层
 *
 * @author makejava
 * @since 2020-04-23 23:02:36
 */
@Mapper
public interface CrawlCompanyInfoDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    CrawlCompanyInfo queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<CrawlCompanyInfo> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param crawlCompanyInfo 实例对象
     * @return 对象列表
     */
    List<CrawlCompanyInfo> queryAll(CrawlCompanyInfo crawlCompanyInfo);

    /**
     * 新增数据
     *
     * @param crawlCompanyInfo 实例对象
     * @return 影响行数
     */
    int insert(CrawlCompanyInfo crawlCompanyInfo);

    /**
     * 修改数据
     *
     * @param crawlCompanyInfo 实例对象
     * @return 影响行数
     */
    int update(CrawlCompanyInfo crawlCompanyInfo);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}
package com.zdb.modules.crawl.controller;

import com.zdb.modules.crawl.entity.CrawlCompanyInfo;
import com.zdb.modules.crawl.service.CrawlCompanyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (CrawlCompanyInfo)表控制层
 *
 * @author makejava
 * @since 2020-04-23 23:02:38
 */
@RestController
@RequestMapping("crawlCompanyInfo")
public class CrawlCompanyInfoController {
    /**
     * 服务对象
     */
    @Autowired
    private CrawlCompanyInfoService crawlCompanyInfoService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public CrawlCompanyInfo selectOne(Long id) {
        return this.crawlCompanyInfoService.queryById(id);
    }

}
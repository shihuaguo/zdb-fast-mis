package com.zdb;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdb.common.utils.SpringContextUtils;
import com.zdb.modules.crawl.entity.CrawlCompanyInfo;
import com.zdb.modules.crawl.service.CrawlCompanyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/* 解析监听器，
 * 每解析一行会回调invoke()方法。
 * 整个excel解析结束会执行doAfterAllAnalysed()方法
 *
 * 下面只是我写的一个样例而已，可以根据自己的逻辑修改该类。
 * @author jipengfei
 * @date 2017/03/14
 */
@Component
public class ExcelListener extends AnalysisEventListener {

    @Autowired
    CrawlCompanyInfoService crawlCompanyInfoService ;

    //自定义用于暂时存储data。
    //可以通过实例获取该值
    private List<Object> datas = new ArrayList<Object>();
    private String importPubKey =UUID.randomUUID().toString();
    public void invoke(Object object, AnalysisContext context) {
//        System.out.println("当前sheetName="+context.getCurrentSheet().getSheetName() +" 当前行："+context.getCurrentRowNum());
//        System.out.println(object);
        datas.add(object);//数据存储到list，供批量处理，或后续自己业务逻辑处理。
        doSomething(object);//根据自己业务做处理
    }
    private void doSomething(Object object) {
        //1、入库调用接口
//        String jsonObject = JSON.toJSONString(object) ;
//        datas.add(jsonObject);
    }
    public void doAfterAllAnalysed(AnalysisContext context) {
        System.out.println("importPubKey=="+importPubKey);
        System.out.println("解析结束销毁不用的资源"+datas.size());
        String dataJson = JSON.toJSONString(datas) ;
        System.out.println("dataJson = " + dataJson);
        CrawlCompanyInfo crawlCompanyInfo =
                CrawlCompanyInfo.builder()
                        .companyInfo(dataJson)
                        .createTime(new Date())
                        .updateTime(new Date())
                        .flag(0)
                        .excelImportKey(importPubKey)
                        .build() ;
        crawlCompanyInfoService = SpringContextUtils.getBean(CrawlCompanyInfoService.class);
        crawlCompanyInfoService.insert(crawlCompanyInfo);
        datas.clear();//解析结束销毁不用的资源
    }
    public List<Object> getDatas() {
        return datas;
    }
    public void setDatas(List<Object> datas) {
        this.datas = datas;
    }
}
package com.zdb.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

/**
 * 和微信服务器通信工具类
 * @author shihuaguo
 * @email huaguoshi@gmail.com
 * @date 2018-12-24
 **/
@Slf4j
public class WxUtil {
    private static final String WEIXIN_API_QUERY_BY_JSCODE = "https://api.weixin.qq.com/sns/jscode2session";

    public static JSONObject queryUserByJsCode(String appId, String secret, String jsCode){
        String url = WEIXIN_API_QUERY_BY_JSCODE + "?appid=" + appId + "&secret=" + secret + "&js_code="
                + jsCode + "&grant_type=authorization_code";
        String res = HttpClientUtil.doGet(url);
        log.info("根据js_code请求用户信息,返回={}", res);
        return StringUtils.isNotBlank(res) ? JSON.parseObject(res) : null;
    }
}

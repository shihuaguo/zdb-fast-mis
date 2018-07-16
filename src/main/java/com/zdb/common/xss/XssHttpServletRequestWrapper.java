package com.zdb.common.xss;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * XSS过滤处理
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-01 11:29
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private static final Logger logger = LoggerFactory.getLogger(XssHttpServletRequestWrapper.class);
    //没被包装过的HttpServletRequest（特殊场景，需要自己过滤）
    HttpServletRequest orgRequest;
    //html过滤
    private final static HTMLFilter htmlFilter = new HTMLFilter();
    
    //忽略的url
    private final static Map<String, Object> FILTER_IGNORE_URL = new HashMap<>();
    //private final static String[] FILTER_IGNORE_URL = {"/financial/ueditor/save","/financial/taxic/feedback/update","/financial/taxic/feedback/save"};
    static {
    	FILTER_IGNORE_URL.put("/financial/ueditor/save", "");
    	FILTER_IGNORE_URL.put("/financial/taxic/feedback/update", "");
    	FILTER_IGNORE_URL.put("/financial/taxic/feedback/save", "");
    	FILTER_IGNORE_URL.put("/financial/customer/updateCri", "");
    }
    
    //忽略的url部分,只要url包含该部分即忽略
    //private final static String[] FILTER_IGNORE_URL_CONTAIN = {};

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        orgRequest = request;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        //非json类型，直接返回
        if(!MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(super.getHeader(HttpHeaders.CONTENT_TYPE))){
            return super.getInputStream();
        }
        
        //System.out.println("request uri=" + super.getRequestURI());

        //为空，直接返回
        String json = IOUtils.toString(super.getInputStream(), "utf-8");
        if (StringUtils.isBlank(json)) {
            return super.getInputStream();
        }

        //xss过滤
        json = xssEncode(json);
        final ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes("utf-8"));
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return true;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] parameters = super.getParameterValues(name);
        if (parameters == null || parameters.length == 0) {
            return null;
        }
        //对于特定的url,不做处理
        //String requestUri = getRequestURI();
        
//        boolean ignore = false;
//        for(String ignoredUrl : FILTER_IGNORE_URL) {
//        	if(requestUri.indexOf(ignoredUrl) >= 0) {
//        		ignore = true;
//        		break;
//        	}
//        }
        //if(!ignore) {
        	for (int i = 0; i < parameters.length; i++) {
        		parameters[i] = xssEncode(parameters[i]);
        	}
//        }else {
//        	logger.info("requestUri={},ignore filter for xss", requestUri);
//        }
        	

        return parameters;
    }

    @Override
    public Map<String,String[]> getParameterMap() {
        Map<String,String[]> map = new LinkedHashMap<>();
        Map<String,String[]> parameters = super.getParameterMap();
        for (String key : parameters.keySet()) {
            String[] values = parameters.get(key);
            for (int i = 0; i < values.length; i++) {
                values[i] = xssEncode(values[i]);
            }
            map.put(key, values);
        }
        return map;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(xssEncode(name));
        if (StringUtils.isNotBlank(value)) {
            value = xssEncode(value);
        }
        return value;
    }

    private String xssEncode(String input) {
    	String requestUri = getRequestURI();
    	//对于有些url,忽略
    	if(!FILTER_IGNORE_URL.containsKey(requestUri)) {
    		return htmlFilter.filter(input);
    	}else {
    		logger.info("requestUri={},ignore filter for xss", requestUri);
    		return input;
    	}
    }

    /**
     * 获取最原始的request
     */
    public HttpServletRequest getOrgRequest() {
        return orgRequest;
    }

    /**
     * 获取最原始的request
     */
    public static HttpServletRequest getOrgRequest(HttpServletRequest request) {
        if (request instanceof XssHttpServletRequestWrapper) {
            return ((XssHttpServletRequestWrapper) request).getOrgRequest();
        }

        return request;
    }

}

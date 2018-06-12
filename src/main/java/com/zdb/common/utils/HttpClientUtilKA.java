package com.zdb.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http client util with keep alive
 * 
 * @author Administrator
 *
 */
public class HttpClientUtilKA {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtilKA.class);
	
	private CloseableHttpClient getHttpClient() {
		return SingletonHolder.httpclient;
	}
	
	private static class SingletonHolder{
		static CloseableHttpClient httpclient;
		static {
			logger.info("initialize httpclient...");
			httpclient = HttpClients.createDefault();
		}
	}
	
	public void cloes() {
		CloseableHttpClient httpclient = getHttpClient();
		IOUtils.closeQuietly(httpclient);
	}
	
	private static final URI buildURI(String url, Map<String, String> param) throws URISyntaxException {
		// 创建uri
		URIBuilder builder = new URIBuilder(url);
		if (param != null) {
			for (String key : param.keySet()) {
				builder.addParameter(key, param.get(key));
			}
		}
		URI uri = builder.build();
		return uri;
	}
	
	public String doGet(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = getHttpClient();
		CloseableHttpResponse response = null;
		try {
			URI uri = buildURI(url, param);
			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);
			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(response.getEntity(), "UTF-8");
			}else {
				logger.error("http get return error,url={},param={}, status code={}", url, param, response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			logger.error("http get throws exception, url={}", url, e);
		} finally {
			IOUtils.closeQuietly(response);
		}
		return null;
	}
	
	public String doGet1(String url, Map<String, Object> param) {
		Map<String, String> param1 = new HashMap<>();
		for(Map.Entry<String, Object> entry : param.entrySet()) {
			param1.put(entry.getKey(), entry.getValue().toString());
		}
		return doGet(url, param1);
	}
	
	/**
	 * 下载文件
	 * @param url
	 * @param param
	 * @return 本地文件保存的路径
	 */
	public String doDownload(String url, Map<String, String> param) {
		// 创建Httpclient对象
		CloseableHttpClient httpclient = getHttpClient();
		CloseableHttpResponse response = null;
		try {
			URI uri = buildURI(url, param);
			// 创建http GET请求
			HttpGet httpGet = new HttpGet(uri);
			// 执行请求
			response = httpclient.execute(httpGet);
			// 判断返回状态是否为200
			if (response.getStatusLine().getStatusCode() == 200) {
				File file = File.createTempFile("valid_code", ".png");
				try (OutputStream os = new FileOutputStream(file)){
					InputStream is = response.getEntity().getContent();
					IOUtils.copy(is, os);
					return file.getPath();
				} catch (Exception e) {
					logger.error("http download file error, url={}", url, e);
				}
			}else {
				logger.error("http download return error,url={},param={}, status code={}", url, param, response.getStatusLine().getStatusCode());
			}
		} catch (Exception e) {
			logger.error("http download throws exception, url={}", url, e);
		} finally {
			IOUtils.closeQuietly(response);
		}
		return null;
	}
	
	public String doPostJson(String url, String json) {
		// 创建Httpclient对象
		CloseableHttpClient httpClient = getHttpClient();
		CloseableHttpResponse response = null;
		String resultString = "";
		try {
			// 创建Http Post请求
			HttpPost httpPost = new HttpPost(url);
			// 创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			// 执行http请求
			response = httpClient.execute(httpPost);
			resultString = EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			logger.error("http port json throws exception, url={},json={}", url, json, e);
		} finally {
			IOUtils.closeQuietly(response);
		}

		return resultString;
	}
	
	public static void main(String[] args) throws Exception {
		String baseUrl = "http://gsxt.gzaic.gov.cn/aiccips/GZpublicity/getCode.html?random=";
		String url = baseUrl + Math.random();
		HttpClientUtilKA ka = new HttpClientUtilKA();
		String file = ka.doDownload(url, null);
		System.out.println("download valid code1, path: " + file);
		
		Thread.sleep(3000);
//		String res = HttpClientUtil.doGet(url);
//		System.out.println(res);
		
		url = baseUrl + Math.random();
		file = ka.doDownload(url, null);
		System.out.println("download valid code2, path: " + file);
		
		ka.cloes();
	}
}

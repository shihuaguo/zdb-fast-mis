package com.zdb.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 商事主体信息查询平台util类
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2018-07-15
 *
 */
public class CriUtil {

	private static final Logger logger = LoggerFactory.getLogger(CriUtil.class);
	
	public static final String BASE_URL_CRI = "http://cri.gz.gov.cn";
	
	/**
	 * 获取验证码图片地址和guid
	 * @return	[0]-validate url [1]-guid
	 */
	public static R getValidateCodeUrl(HttpClientUtilKA ha) {
		long time = System.currentTimeMillis();
		String url = CriUtil.BASE_URL_CRI + "/Search/NewGuid?t=" + time;
		
		String res = ha.doGet1(url, new HashMap<String, Object>());
		
		StringBuilder sb = new StringBuilder();
		sb.append(BASE_URL_CRI).append("/Search/ValidateCode?t=").append(time);
		sb.append("&guid=").append(res);
		
		if(logger.isInfoEnabled()) {
			logger.info("ValidateCode url={}, guid={}", sb.toString(), res);
		}
		
		return R.ok().put("guid", res).put("url", sb.toString());
	}
	
	public static R syncCri(String socialReditOde, String validCode, String guid, HttpClientUtilKA ha) {
		StringBuilder url = new StringBuilder();
		url.append(CriUtil.BASE_URL_CRI).append("/Search/Result?");
		url.append("validateCode=").append(validCode);
		url.append("&guid=").append(guid);
		url.append("&keywords=").append(socialReditOde);
		
		String res = ha.doGet(url.toString(), new HashMap<>());
		if(StringUtils.isNotBlank(res)) {
			return parseSearchResult(url.toString(), socialReditOde, res, ha);
		}
		
		return R.error("get from url " + url.toString() + " return null");
	}
	
	/**
	 * 解析根据关键字搜索的返回信息
	 * @param res
	 * @param ha
	 * @return
	 */
	private static R parseSearchResult(String url, String socialReditOde, String res, HttpClientUtilKA ha) {
		Parser parser;
		HasAttributeFilter filter = new HasAttributeFilter("target", "_blank");
		try {
			parser = new Parser(res);
			NodeList nodes = parser.parse(filter);
			if(nodes != null && nodes.size() > 0) {
				Node node = nodes.elementAt(0);
				//logger.info("node={}", node);
				if(node instanceof LinkTag) {
					LinkTag lt = (LinkTag)node;
					String link = lt.getLink();
					logger.info("link={}", link);
					res = ha.doGet(CriUtil.BASE_URL_CRI + link, new HashMap<>());
					
					logger.info("获取{}商事信息,url={},返回={}", socialReditOde, (CriUtil.BASE_URL_CRI + link), res);
					return parseDetail(CriUtil.BASE_URL_CRI + link, res, ha);
				}
			}else {
				return R.error("同步失败,请检查验证码是否输入正确");
			}
		} catch (ParserException e) {
			logger.error("", e);
			return R.error("同步失败,error=" + e.getMessage());
		}
		return R.ok();
	}
	
	private static R parseDetail(String url, String res, HttpClientUtilKA ha) {
		Parser parser;
		//HasAttributeFilter filter = new HasAttributeFilter("id", "DetailContent");
		TagNameFilter filter = new TagNameFilter("table");
		try {
			parser = new Parser(res);
			NodeList list = parser.parse(filter);
			if(list != null && list.size() > 0) {
				Node node = list.elementAt(0);
				list = node.getChildren();
				logger.info("table's children's size={}", list.size());
				if(list != null) {
					int trCount = 0;
					for(int i = 0; i < list.size(); i++) {
						node = list.elementAt(i);
						if(node instanceof TableRow) {
							TableRow tr = (TableRow)node;
							if(trCount == 14) {
								logger.info("tr={}", tr);
								list = tr.getChildren();
								if(list != null && list.size() >= 4) {
									TableColumn tc = (TableColumn)list.elementAt(3);
									list = tc.getChildren();
									if(list != null && list.size() > 0) {
										TextNode tn = (TextNode)list.elementAt(0);
										String status = StringUtils.trim(tn.getText());
										logger.info("text node ={}", status);
										Map<String, Object> customerCri = new HashMap<>();
										customerCri.put("businessStatus", status);
										R r = R.ok().put("customerCri", customerCri);
										
										//查询企业的异常信息
										url = url.replace("Detail", "Detail/AbnormalList");
										res = ha.doGet(url, new HashMap<>());
										logger.info("查询企业异常信息,url={},返回={}", url,res);
										customerCri.put("AbnormalList", res);
										return r;
									}
								}
							}
							trCount++;
						}
					}
				}
			}
			//logger.info("list={}", list);
		} catch (ParserException e) {
			logger.error("", e);
		}
		return R.error("解析失败");
	}
	
	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		String url = CriUtil.BASE_URL_CRI + "Search/NewGuid?t=" + time;
		
		HttpClientUtilKA ha = new HttpClientUtilKA();
		String res = ha.doGet1(url, new HashMap<String, Object>());
		
		System.out.println("res=" + res);
		System.out.println("ValidateCode url = " + (BASE_URL_CRI + "Search/ValidateCode?t=" + time + "&guid=" + res));
	}
}

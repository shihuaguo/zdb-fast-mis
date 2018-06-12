/**
 * 
 */
package com.zdb.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.LinkStringFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdb.modules.customer.entity.Customer;
import com.zdb.modules.customer.entity.CustomerIndustryCommerce;

/**
 * 
 * 从广州市工商行政管理局抓取信息的实用类
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2017年10月9日
 */
public class GZpublicityUtil {
	private static final Logger logger = LoggerFactory.getLogger(GZpublicityUtil.class);
	
	public static final String BASE_URL_GZpublicity = "http://gsxt.gzaic.gov.cn/aiccips/GZpublicity";
	
	//获取工商登记信息验证码的url
	public static final String URL_validecodeof_ic = BASE_URL_GZpublicity + "/getCode.html?random=";
	//在广州市工商行政管理局验证验证码
	public static final String URL_checkcodeof_ic = BASE_URL_GZpublicity + "/checkCodeGz.html?entName=&regNo=&text3=";
	
	//获取工商登记信息的地址
	public static final String URL_showentof_ic = BASE_URL_GZpublicity + "/showEnt.html";
	
	public static final String URL_loginfoof_ic = BASE_URL_GZpublicity + "/GZpublicityList.html";
	
	
	public static R syncIndAndComInfo(String socialReditOde, String validCode, HttpClientUtilKA kd) {
		String url = URL_checkcodeof_ic + socialReditOde + "&code=" + validCode;
		String res = kd.doGet(url, null);
		logger.info("对验证码进行验证，返回结果={}", res);
		JSONObject obj = (JSONObject) JSON.parse(res);
		//如果验证码验证成功
		if("1".equals(obj.get("flag"))) {
			logger.info("对验证码进行验证成功,请求获取工商登记信息");
			Map<String, String> param = new HashMap<>();
			param.put("entName", "");
			param.put("regNo", "");
			param.put("uniSCID", socialReditOde);
			param.put("code", validCode);
			res = kd.doGet(URL_showentof_ic, param);
			//logger.info(res);
			return parseByshowEnt(res, kd);
		}else {
			return R.error("验证码错误");
		}
	}
	
	/**
	 * 解析由页面"http://gsxt.gzaic.gov.cn/aiccips/GZpublicity/showEnt.html"返回的信息
	 * @param res
	 * @return
	 */
	public static R parseByshowEnt(String res, HttpClientUtilKA kd) {
		Parser parser;
		try {
			parser = new Parser(res);
			LinkStringFilter filter = new LinkStringFilter(URL_loginfoof_ic);
			NodeList nodes = parser.extractAllNodesThatMatch(filter); 
			String icInfo = null;
			if(nodes!=null) {
				for (int i = 0; i < nodes.size(); i++) {
					Node textnode = (Node) nodes.elementAt(i);
					if(textnode instanceof LinkTag) {
						LinkTag linkTag = (LinkTag)textnode;
						String link = linkTag.getLink();
						logger.info("从showEnt.html返回的网页中解析到最终URL={}", link);
						//获取最终的工商登记信息
						icInfo = kd.doGet(link, null);
					}
				}
			}
			if(icInfo == null) {
				return R.error("解析showEnt.html返回信息未找到link");
			}else {
				return parseByGZpublicityList(icInfo);
			}
		} catch (ParserException e) {
			logger.error("解析由{}返回的信息失败", URL_showentof_ic, e);
			return R.error("解析由" + URL_showentof_ic + "返回的信息失败");
		}
	}
	
	/**
	 * 解析由页面"http://gsxt.gzaic.gov.cn/aiccips/GZpublicity/GZpublicityList.html"返回的信息
	 * 返回Customer和CustomerIndustryCommerce的信息
	 * @param res
	 * @return
	 */
	public static R parseByGZpublicityList(String res) {
		Parser parser;
		try {
			parser = new Parser(res);
			HasAttributeFilter filter = new HasAttributeFilter("class", "trl_basic_ent");
			NodeList nodes = parser.extractAllNodesThatMatch(filter); 
			CustomerIndustryCommerce customerIc = null;
			Customer customer = null;
			if(nodes!=null) {
	            for (int i = 0; i < nodes.size(); i++) {
	                Node node = (Node) nodes.elementAt(i);
	                if(node instanceof Div) {
	                	Div div = (Div)node;
	                	NodeList nodeList = div.getChildren();
	                	for(int j = 0; j < nodeList.size(); j++) {
	                		Node node1 = nodeList.elementAt(j);
	                		if(node1 instanceof TableTag) {
	                			customerIc = new CustomerIndustryCommerce();
	                			customer = new Customer();
	                			TableTag tt = (TableTag)node1;
	                			TableRow[] trs = tt.getRows();
	                			for(int k = 0; k < trs.length; k++) {
	                				TableColumn[] tcs =  trs[k].getColumns();
	                				for(int p = 0; p < tcs.length; p++) {
	                					String html = tcs[p].toHtml();
	                					String plainText = tcs[p].toPlainTextString();
	                					if("名称".equals(plainText) && tcs.length > (p+1)) {
	                						customer.setCustomerName(tcs[p+1].toPlainTextString());
	                					}else if("地址".equals(plainText) && tcs.length > (p+1)) {
	                						customerIc.setRegisterAddr(tcs[p+1].toPlainTextString());
	                					}else if("注册号".equals(plainText) && tcs.length > (p+1)) {
	                						customerIc.setTaxIdNumber(tcs[p+1].toPlainTextString());
	                					}else if("统一社会信用代码".equals(plainText) && tcs.length > (p+1)) {
		                					customerIc.setSocialReditOde(tcs[p+1].toPlainTextString());
		                				}else if("法定代表人".equals(plainText) && tcs.length > (p+1)) {
		                					customer.setLegalPerson(tcs[p+1].toPlainTextString());
		                				}else if("成立日期".equals(plainText) && tcs.length > (p+1)) {
		                					customerIc.setStartDate(tcs[p+1].toPlainTextString());
		                				}else if("注册资本".equals(plainText) && tcs.length > (p+1)) {
		                					customerIc.setRegisterCapital(tcs[p+1].toPlainTextString());
		                				}else if("注册资本".equals(plainText) && tcs.length > (p+1)) {
		                					customerIc.setRegisterCapital(tcs[p+1].toPlainTextString());
		                				}else if("年度报告情况".equals(plainText) && tcs.length > (p+1)) {
		                					customerIc.setAnnualReport(tcs[p+1].toPlainTextString());
		                				}else if("营业期限".equals(plainText) && tcs.length > (p+1)) {
		                					String businessTerm = tcs[p+1].toPlainTextString();
		                					customerIc.setBusinessTerm(StringUtils.trim(businessTerm));
		                				}else if("股东情况".equals(plainText) && tcs.length > (p+1)) {
		                					//股东信息是一个单独的表格
		                					//logger.info("" + tcs[p+1].getClass());
		                					//String businessTerm = tcs[p+1].toPlainTextString();
		                					//customerIc.setBusinessTerm(StringUtils.trim(businessTerm));
		                					StringBuilder shareHolder = new StringBuilder();
		                					NodeList nodeList2 = tcs[p+1].getChildren();
		                					if(nodeList2 != null) {
		                						for(int t = 0; t < nodeList2.size(); t++) {
		                							Node node2 = nodeList2.elementAt(t);
		                							if(node2 instanceof TableTag) {
		                								TableTag tag2 = (TableTag)node2;
		                								TableRow[] trs2 = tag2.getRows();
		                								for(int o = 1; o < trs2.length; o++) {
		                									TableColumn[] tcs2 = trs2[o].getColumns();
		                									if(tcs2.length >= 1) {
		                										shareHolder.append(tcs2[0].toPlainTextString()).append(",");
		                									}
		                								}
		                							}
		                						}
		                					}
		                					String sh = shareHolder.toString();
		                					if(sh.length() > 0) {
		                						logger.info("股东信息：{}", sh);
		                						customerIc.setShareholder(sh.substring(0, sh.length()-1));
		                					}
		                				}
	                					
	                					logger.info(html);
	                				}
	                				logger.info("\n");
	                			}
	                		}
	                	}
	                }else {
	                	logger.info("i=" + i + ", node is " + node.getClass());
	                }
	                
	            }
	        }
			if(customerIc == null || customer == null) {
				return R.error("未从GZpublicityList.html返回信息解析出工商登记信息");
			}
			return R.ok().put("customerIc", customerIc).put("customer", customer);
		} catch (ParserException e) {
			logger.error("解析由'{}'返回的信息失败", URL_loginfoof_ic, e);
			return R.error("解析由" + URL_loginfoof_ic + "返回的信息失败");
		}
	}
}

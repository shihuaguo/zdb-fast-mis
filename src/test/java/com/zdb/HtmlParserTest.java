package com.zdb;

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
import org.junit.Test;

public class HtmlParserTest {

	//@Test
	public void test() throws ParserException {
		String html = "<script>\r\n" + 
				"    /*function toPage(pageNo){\r\n" + 
				"        document.getElementById(\"pageNo\").value=pageNo;\r\n" + 
				"        document.getElementById(\"searchFlag\").value='no';\r\n" + 
				"\r\n" + 
				"        var entName = $(\"#entName\").val();\r\n" + 
				"        var regNo = $(\"#regNo\").val();\r\n" + 
				"        var uniSCID = $(\"#uniSCID\").val();\r\n" + 
				"        var url = \"http://gsxt.gzaic.gov.cn/aiccips/GZpublicity/checkNoCode.html\";\r\n" + 
				"        showLoading();\r\n" + 
				"        $(\"#showEntID\").load(url,{entName:entName,regNo:regNo,uniSCID:uniSCID,pageNo:pageNo},function(){\r\n" + 
				"            hidenLoading();\r\n" + 
				"            clickSee();\r\n" + 
				"        });\r\n" + 
				"        document.getElementById('con1Show').style.display=\"none\";\r\n" + 
				"        document.getElementById('showEntID').style.display=\"block\";\r\n" + 
				"    }*/\r\n" + 
				"</script>\r\n" + 
				"<table cellpadding=\"0\" cellspacing=\"0\" style=\"width: 990px\">\r\n" + 
				"    <tr>\r\n" + 
				"        <th width=\"18%\">注册号/统一社会信用代码</th>\r\n" + 
				"        <th width=\"30%\">名称</th>\r\n" + 
				"        <th width=\"30%\">住所</th>\r\n" + 
				"        <th width=\"10%\">状态</th>\r\n" + 
				"        <th width=\"12%\">详细</th>\r\n" + 
				"    </tr>\r\n" + 
				"           <tr>\r\n" + 
				"               <td>91440101579980676J</td>\r\n" + 
				"               <td>广州景峰网络科技有限公司</td>\r\n" + 
				"               <td>广州市天河区棠东东路13号B201</td>\r\n" + 
				"               <td>已开业</td>\r\n" + 
				"               <td><a target=\"_blank\" href=\"http://gsxt.gzaic.gov.cn/aiccips/GZpublicity/GZpublicityList.html?entNo=440101101022011080900090&regOrg=&service=entInfo\">详情</a></td>\r\n" + 
				"           </tr>\r\n" + 
				"</table>";
		Parser parser = new Parser(html);
		LinkStringFilter filter = new LinkStringFilter("http://gsxt.gzaic.gov.cn/aiccips/GZpublicity/GZpublicityList.html");
		NodeList nodes = parser.extractAllNodesThatMatch(filter); 
        
        if(nodes!=null) {
            for (int i = 0; i < nodes.size(); i++) {
                Node textnode = (Node) nodes.elementAt(i);
                
                System.out.println(textnode.getClass());
                if(textnode instanceof LinkTag) {
                	LinkTag linkTag = (LinkTag)textnode;
                	System.out.println(linkTag.getLink());
                }
                
                System.out.println("getText:"+textnode.getText());
                System.out.println("=================================================");
            }
        }            
	}
	
	@Test
	public void test2() throws ParserException {
		String html = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\r\n" + 
				"        \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\r\n" + 
				"<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + 
				"<head>\r\n" + 
				"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n" + 
				"    <title>广州市工商行政管理局</title>\r\n" + 
				"    <link rel=\"stylesheet\" type=\"text/css\" href=\"http://gsxt.gzaic.gov.cn/aiccips/css/base.css\"/>\r\n" + 
				"    <script type=\"text/javascript\" src=\"http://gsxt.gzaic.gov.cn/aiccips/js/jquery-1.10.2.min.js\"></script>\r\n" + 
				"    <script type=\"text/javascript\">\r\n" + 
				"        var width = document.documentElement.clientWidth*0.75; // 可见区域宽度\r\n" + 
				"        var height = document.documentElement.clientHeight*0.85;// 可见区域高度\r\n" + 
				"\r\n" + 
				"        function fnSetValues(){\r\n" + 
				"            var sFeatures=\"dialogHeight: \" + height + \"px;\";\r\n" + 
				"            sFeatures+=\"dialogWidth: \" + width + \"px;\";\r\n" + 
				"            return sFeatures;\r\n" + 
				"        }\r\n" + 
				"       function printEnt(entNo,regOrg,falg){\r\n" + 
				"              var pageUrl=\"http://gsxt.gzaic.gov.cn/aiccips/GZpublicity/printShow.html?entNo=440101101022011080900090&regOrg=440106\"\r\n" + 
				"              window.showModalDialog(pageUrl,'',fnSetValues());\r\n" + 
				"       }\r\n" + 
				"    </script>\r\n" + 
				"</head>\r\n" + 
				"\r\n" + 
				"<body>\r\n" + 
				"    <div class=\"w\">\r\n" + 
				"        <div class=\"ctrl_header\">\r\n" + 
				"            <img src=\"http://gsxt.gzaic.gov.cn/aiccips/img/banner.jpg\" width=\"1000\" height=\"120\">\r\n" + 
				"        </div>\r\n" + 
				"    <div class=\"ctrl_nav_ent clearfix\">\r\n" + 
				"        <ul class=\"float-l\">\r\n" + 
				"            <li><a class=\"cur\" style=\"margin-left:-5px;\" href=\"http://gsxt.gzaic.gov.cn/aiccips/GZpublicity/GZpublicityList.html?entNo=440101101022011080900090&regOrg=440106&service=entInfo\">企业基本信息</a></li>\r\n" + 
				"            <li><a href=\"http://gsxt.gzaic.gov.cn/aiccips/GZpublicity/GZpublicityList.html?entNo=440101101022011080900090&regOrg=440106&service=pleInfo\">动产抵押信息</a></li>\r\n" + 
				"            <li><a href=\"http://gsxt.gzaic.gov.cn/aiccips/GZpublicity/GZpublicityList.html?entNo=440101101022011080900090&regOrg=440106&service=courtFreeze\">司法协助信息</a></li>\r\n" + 
				"        </ul>\r\n" + 
				"        <div class=\"float-r abnormal\">\r\n" + 
				"            广州景峰网络科技有限公司&nbsp;&nbsp;&nbsp;&nbsp;注册号：440101000174713\r\n" + 
				"        </div>\r\n" + 
				"    </div>\r\n" + 
				"    <div class=\"trl_basic_ent\">\r\n" + 
				"        <h3><input style=\"font-weight: bolder\" type=\"button\" value=\"打印\" onclick=\"printEnt('440101101022011080900090','440106','entInfo')\"/>商事登记基本信息</h3>\r\n" + 
				"        <table  cellpadding=\"0\" cellspacing=\"0\" >\r\n" + 
				"            <tr>\r\n" + 
				"                <td><span>名称</span></td>\r\n" + 
				"                <td colspan=\"3\" class=\"bg\">广州景峰网络科技有限公司</td>\r\n" + 
				"\r\n" + 
				"            </tr>\r\n" + 
				"            <tr>\r\n" + 
				"                <td><span>地址</span></td>\r\n" + 
				"                <td colspan=\"3\" class=\"bg\">广州市天河区棠东东路13号B201</td>\r\n" + 
				"            </tr>\r\n" + 
				"            <tr>\r\n" + 
				"                <td><span>注册号</span></td>\r\n" + 
				"                <td colspan=\"3\" class=\"bg\">440101000174713</td>\r\n" + 
				"            </tr>\r\n" + 
				"            <tr>\r\n" + 
				"                <td><span>统一社会信用代码</span></td>\r\n" + 
				"                <td colspan=\"3\" class=\"bg\">91440101579980676J</td>\r\n" + 
				"            </tr>\r\n" + 
				"            <tr>\r\n" + 
				"                <td style=\"width: 14%\"><span>法定代表人</span></td>\r\n" + 
				"                <td style=\"width: 36%\"  class=\"bg\">郑少烽</td>\r\n" + 
				"                <td style=\"width: 14%\"><span>商事主体类型</span></td>\r\n" + 
				"                <td style=\"width: 36%\" class=\"bg\">有限责任公司(自然人投资或控股)</td>\r\n" + 
				"            </tr>\r\n" + 
				"            <tr>\r\n" + 
				"                <td><span>成立日期</span></td>\r\n" + 
				"                <td class=\"bg\">2011年08月10日</td>\r\n" + 
				"                <td><span>主体状态</span></td>\r\n" + 
				"                <td class=\"bg\">已开业</td>\r\n" + 
				"            </tr>\r\n" + 
				"            <tr>\r\n" + 
				"                <td><span>注册资本</span></td>\r\n" + 
				"                <td class=\"bg\">1000万</td>\r\n" + 
				"                <td><span>登记机关</span></td>\r\n" + 
				"                <td class=\"bg\">广州市天河区工商行政管理局</td>\r\n" + 
				"            </tr>\r\n" + 
				"            <tr>\r\n" + 
				"\r\n" + 
				"                <td><span>年度报告情况</span></td>\r\n" + 
				"                <td colspan=\"3\" class=\"bg\">2016年度已提交;2015年度已提交;2014年度已提交;</td>\r\n" + 
				"            </tr>\r\n" + 
				"            <tr>\r\n" + 
				"                <td><span>核准日期</span></td>\r\n" + 
				"                <td class=\"bg\">2016年12月30日</td>\r\n" + 
				"                <td><span>营业期限</span></td>\r\n" + 
				"                <td class=\"bg\">\r\n" + 
				"                    2011年08月10日至\r\n" + 
				"                    长期\r\n" + 
				"                </td>\r\n" + 
				"            </tr>\r\n" + 
				"\r\n" + 
				"            <tr >\r\n" + 
				"                <td><span>经营范围</span></td>\r\n" + 
				"                <td colspan=\"3\" class=\"bg\">研究和试验发展(具体经营项目请登录广州市商事主体信息公示平台查询。依法须经批准的项目，经相关部门批准后方可展开经营活动。)</td>\r\n" + 
				"            </tr>\r\n" + 
				"                <tr>\r\n" + 
				"                           <td><span>股东情况</span></td>\r\n" + 
				"                    <td class=\"bg\" colspan=\"3\" >\r\n" + 
				"                        <table class=\"table_inside\" cellpadding=\"0\" cellspacing=\"0\">\r\n" + 
				"                            <tbody>\r\n" + 
				"                            <tr>\r\n" + 
				"                                <th width=\"100%\">股东名称</th>\r\n" + 
				"                            </tr>\r\n" + 
				"\r\n" + 
				"                                            <tr>\r\n" + 
				"                                                <td>郑少烽</td>\r\n" + 
				"                                            </tr>\r\n" + 
				"                                            <tr>\r\n" + 
				"                                                <td>郑少荣</td>\r\n" + 
				"                                            </tr>\r\n" + 
				"                            </tbody>\r\n" + 
				"                        </table>\r\n" + 
				"                    </td>\r\n" + 
				"                </tr>\r\n" + 
				"            <tr>\r\n" + 
				"                <td><span>备注</span></td>\r\n" + 
				"                <td colspan=\"3\" class=\"bg\"></td>\r\n" + 
				"            </tr>\r\n" + 
				"        </table>\r\n" + 
				"    </div>\r\n" + 
				"    <div class=\"x_height\"></div>\r\n" + 
				"    <div class=\"ctrl_foot\">\r\n" + 
				"        <ul class=\"clearfix\">\r\n" + 
				"            <li><a href=\"javascript:void(0)\">设为首页</a><span>|</span></li>\r\n" + 
				"            <li><a href=\"javascript:void(0)\">加入收藏</a></li>\r\n" + 
				"        </ul>\r\n" + 
				"        <p>主办单位：广州市工商行政管理局 [粤ICP备030863号]</p>\r\n" + 
				"        <p>任何人任何单位不得以任何方式复制或变相复制本网全部或部分信息</p>\r\n" + 
				"    </div>\r\n" + 
				"    </div>\r\n" + 
				"</body>\r\n" + 
				"</html>\r\n" + 
				"";
		
		Parser parser = new Parser(html);
//		CssSelectorNodeFilter filter = new CssSelectorNodeFilter("trl_basic_ent");
		HasAttributeFilter filter = new HasAttributeFilter("class", "trl_basic_ent");
		NodeList nodes = parser.extractAllNodesThatMatch(filter); 
        
		System.out.println("nodes.size()=" + nodes.size());
        if(nodes!=null) {
            for (int i = 0; i < nodes.size(); i++) {
                Node node = (Node) nodes.elementAt(i);
                if(node instanceof Div) {
                	Div div = (Div)node;
                	NodeList nodeList = div.getChildren();
                	for(int j = 0; j < nodeList.size(); j++) {
                		Node node1 = nodeList.elementAt(j);
                		if(node1 instanceof TableTag) {
                			TableTag tt = (TableTag)node1;
                			TableRow[] trs = tt.getRows();
                			for(int k = 0; k < trs.length; k++) {
                				TableColumn[] tcs =  trs[k].getColumns();
                				for(int p = 0; p < tcs.length; p++) {
                					System.out.println(tcs[p].toPlainTextString());
                				}
                				System.out.println();
                			}
                		}
                	}
                }else {
                	System.out.println("i=" + i + ", node is " + node.getClass());
                }
                
            }
        }         
	}
}

package com.zdb.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdb.modules.customer.entity.CustomerTax;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.Base64.Encoder;

/**
 * 从税局抓取信息的实用类
 *
 * @author shihuaguo
 * @email 147402691@qq.com
 * @date 2017年10月9日
 */
public class EtaxUtil {
    private static final Logger logger = LoggerFactory.getLogger(EtaxUtil.class);

    //税局基础地址
    private static final String BASE_URL_etax = "https://www.etax-gd.gov.cn";

    //税局基础地址https
    private static final String BASE_URL_etax_s = "https://www.etax-gd.gov.cn";
    //登录地址
    private static final String URL_login = BASE_URL_etax + "/sso/login?service=http://www.etax-gd.gov.cn/xxmh/html/index.html?bszmFrom=1&t=";
    //登录验证地址
    private static final String URL_check_login = BASE_URL_etax + "/sso/auth/checkLoginState.do";
    //验证码图片地址
    public static final String URL_checkcode = BASE_URL_etax + "/sso/base/captcha.do?r=";

    //抓取国税地税信息的地址
    private static final String URL_fetch = BASE_URL_etax + "/web-tycx/tycx/query.do";

    //切换纳税人身份
    private static final String URL_changeQySf = BASE_URL_etax + "/sso/auth/changeQySf.do";

    //查询购票员信息
    private static final String URL_queryGpycx = BASE_URL_etax_s + "/gsyw/service/fpyw/gpycx/queryGpycx?yxqq=&yxqz=";

    //spring webflux
//	private static WebClient webClient;
	
	/*private static class CookieHolder{
		//key-cookie path value-have the same path's cookie
		static Map<String, List<ResponseCookie>> cookieMap = new HashMap<>();
		static void init() {
			cookieMap.clear();
		}
		static List<ResponseCookie> getCookies(String path){
			return Optional.ofNullable(cookieMap.get(path)).orElse(new ArrayList<>());
		}
		static void addCookies(MultiValueMap<String, ResponseCookie> cookies) {
			cookies.forEach((s, list) ->{
				list.forEach(rc -> {
					List<ResponseCookie> cs = cookieMap.get(rc.getPath());
					if(cs == null) {
						cs = new ArrayList<>();
						cookieMap.put(rc.getPath(), cs);
					}
					if(!cs.contains(rc)) {
						cs.add(rc);
					}
				});
			});
		}
	}
	
	private static WebClient getWebClient() {
		if (webClient == null) {
			SslContext sslContext;
			try {
				sslContext = SslContextBuilder.forClient().build();
				webClient = WebClient.builder()
						//.defaultHeader("Content-Type", "application/json;charset=UTF-8")
						//.defaultHeader("Accept", "application/json;charset=UTF-8")
						.filter((req, next) -> {
							Builder builder = ClientRequest.from(req);
							String uri = req.url().getPath();
							String context = uri.substring(0, uri.substring(1).indexOf("/") + 2);
							logger.info("request context={}", context);
							// logger.info("CookieHolder's cookieMap={}", CookieHolder.cookieMap);
							List<ResponseCookie> cookieList = CookieHolder.getCookies(context);
							if (context.startsWith("/gsyw")) {
								cookieList.addAll(CookieHolder.getCookies("/web-tycx/"));
							}
							cookieList.addAll(CookieHolder.getCookies("/"));
							// 如果是获取购票员信息
							if (context.startsWith("/gsyw")) {
								//req.headers().setContentType(MediaType.APPLICATION_JSON_UTF8);
								//req.headers().setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
								cookieList = cookieList.stream().filter(rc -> rc.getName().equals("DZSWJ_TGC"))
										.collect(Collectors.toList());
							}

							logger.info("uri = {}, cookieList={}", uri, cookieList);
							cookieList.forEach(rc -> builder.cookie(rc.getName(), rc.getValue()));
							return next.exchange(builder.build());
						}).clientConnector(new ReactorClientHttpConnector(builder -> builder.sslContext(sslContext)))
						.baseUrl(BASE_URL_etax_s).build();
			} catch (SSLException e) {
				logger.error("", e);
			}
		}
		return webClient;
	}
	
	protected static String doGet(String uri, Map<String, String> param) {
//		UriBuilder ub = UriBuilder.
		URIBuilder builder;
		try {
			builder = new URIBuilder(uri);
			if(param != null) {
				param.forEach((k,v) -> builder.addParameter(k, v));
			}
			WebClient webClient = getWebClient();
			return webClient.get().uri(builder.build())
					//.retrieve().bodyToMono(String.class).block();
					.header("Content-Type", "application/json;charset=UTF-8")
					.header("Accept", "application/json;charset=UTF-8")
					.exchange()
					.flatMap(res -> {
						logger.info("cookies={}", res.cookies());
						CookieHolder.addCookies(res.cookies());
						return res.bodyToMono(String.class);
					}).block();
		} catch (URISyntaxException e) {
			logger.error("", e);
		}
		return null;
	}
	
	protected static String doPost(String uri, Map<String, String> param) {
		MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
		param.forEach((k,v) -> mvm.add(k, v));
		return doPost(uri, mvm);
	}
	
	protected static String doPost(String uri, MultiValueMap<String, String> param) {
		return getWebClient().post().uri(uri).body(BodyInserters.fromFormData(param)).retrieve().onStatus(status -> {
			if (status.is4xxClientError() || status.is5xxServerError()) {
				return true;
			}
			return false;
		}, ef -> {
			logger.error("http post return:{}", ef.statusCode());
			return Mono.error(new RRException(ef.statusCode().toString()));
		}).bodyToMono(String.class).block();
	}*/

    /**
     * 构造登录的其他信息
     *
     * @return map
     */
    private static Map<String, String> constructParams() {
        Map<String, String> params = new HashMap<>();
        params.put("lt", "LT-225432-ebJXgZ0Sr0saGBUKL6eC-gddzswj");
        params.put("execution", "4902c759-7c77-409e-9f51-6c844c7e6ba4_ZXlKaGJHY2lPaUpJVXpVeE1pSjkuYVRCNkwxZEdjR1JJTm1wWWRsWkVjRlIwVmtOU2RVYzNUSFJTU0VoalltWldjbkJGUWtJdmJXSldOV3BxTkVkMk5FaHJkMmhFVjNsbGMzVnFSa0p5ZGlzMmRtVmxTV29yUmk5alprUXpiMU53VlZSTE4zTTNjR2t4TUd4SFpYZHZURWR1WW5VMmIxUlZjVFJQVm5JeWNUUm1hMk5GWm1zeVRuRjBObVZ1UVdSblpFUnZiWFZLV21ocE5EZE9hMXBYYlRFelREWnVTMnhtYVVNeU5qRTNSRlZ1VG5CRVozZHFOazE2UVhJeGFtbDJNVEpDZVZnNGFHUnFTMDQ0Y0dsa2JsWnpWREJNVVVaUVpGQXZNWFZzZWpObGMzRklTMnBvTW5ONlZVeDBhbXhzZDNwbWJIZzFXR1pSY1VwQlYybHhSR2xpTlVNME1tMTJVblJwZEhoWllYYzFlbkpKV2pka2Nua3ZhbGczTWxodFZWUnlVR2h5TjFRNWVtcGtlVVZhY2tGMlMwOWpXVlpxTjJoaVVYWjRPVGxSYzFWTFltODBSa3RSVTA1T2MwNVBWRGhxUTB4eE5ISkdWQ3RKYTNrME5uTnJOV3g2V0c1R2VsVnNMMlp1U1VsWGFVdEdjMU0yYnpFdlUyWjBVa0pXUTI1VGIzcG9WVmsxT1VWUVNWbDRWMnBrWkdkS1dqZ3ljREJsUldsUE1IRlpXbXQwV1hKRVVEWXhjbGN2YWs5RlZIUjJaVFE0V0U5VlVrTTVNa3cwUWpkUFJ6Rm1Xa2d6U0ZkalRGTXJXbUptT1VGeGFsZE5kbVl2ZUVNd1ozTXJiM3BOUWxSU1JuSmhZMHMwT1ZGeU5XSkllSGhQZGxKVEwyUkdkWHBsTnpRclNtRnNiM2N2ZFROdWVVeEpiWFpwVWpGd2IzaG9lbUl2WkdzeFIwOTJXVVF2YUU1SFZrVldSVk5qYTFoT1ZtdHVaRk14TTJobk5VTk9TRVZCVm10aWNVOU1ObmhRUWtsRE4yazFOV0pZYlRKcVJtSkdORWR6T0ZsT1RITjBZa3htYlZOb2JsTm1XbGhvZFd4cU5GTXdVR2h4SzNoVFUyNWxNVFZsSzBaTEt6SjBOa0V4UzJaVWFITktWSEppZWpFMVZqTnJSMGhwYjJSMVpYUlFZa1EzTUVsemJraEtjRTFSWVhaSVdTdEZSM3BVWldZeVdFb3hhRnBxVTNGMFJIQm5kalZEZG5KT1UzRXZkalJhUW1sUGNYaE9RVzFoVkVVMVlWTjRjbE01TTFNelJHOW1USE5GV1U5UVJtUlZSMk42Vm05cWVuZGxSMkV5TDNSM1dHTjFNekkwZUhkclRXMXllRkJCT0dOWVVrMXNiMVJ4TjI1UWMySktkU3NyTjFoUFRqTktTRzFOWlZSamIzVkJWM2R2YlhkQ2JVdHlRMlp6VDB4cVRtUmxaek5CT0VWNlREazVOMXBaZUhKUFJuTkNjVTlNVEhwaE5sQjRSRlZyYlV0TVdWWjZOWFZ3VHpVcmIwTkdRMVkxT1ZoRmIza3dRa3RoYkhGdlZHTjFPV2N6YVZkcU1WUnRhWGx1YTJOT2QwaHFZbGxXWWs5d1lYTlNjSGhvYjJKUE4wUTFVVkExWldSRWFWbzBOM1YxZUVFNGIxaGpialZySzB4bFJITnRlV3R1WVdsNVpXdEJWVGRCUkVaUlExbFdWazFoTlhsTlZXYzFVazR2ZG5GbFpHRXZWMlZOVEdKTmVqWjNiMk4wUkdWbGRYcDJTVkFyVEZFME9ESkVkaXRyYW5OcVNWRnhOQzlRUVc5VVVXZElja3hLVG10c2J6TkljblpJZEhJM01HdzFWWGxGUkVSUU9IWmpZVkY0TkUxcVJHbEllRnB1VlVkSFVHaExTa0ZWZDBjd2EyUjFWM2x6U1U5S2MxSXllSFJoUmtGblNIUk5VbXRSWnpKWWJscHJNVmxKU1doNFlYaFpkMHRoYVZaelQzQkxWVFF4ZW5CWk9FUTRkbUpRUTNONlJHWnBVbUZRYjNCNmVUbFpNemMzZFdGRWNuUndVSFp5ZW5WVlprOHJUbk53TlROeVRISlplbE4xV21zNVpHbHVOV3BPWkVjcmNrUndUak13YjFwUmVDc3lNMHAwVlVvMldESkxiSHBOV1hOc1lVOUROVGQ2U3pkalpIaE9VV0U1YTJnd1ZHMHpWMDQ1Unl0NGJYVk1RbXhKYmtReFRXbHZTM05tVm5GT1ZteDRPRmx6Ym1wMGVHZE1TemgwTlU5aU5rWllPRkExU25Ndk0wSkhNVlZZVTIxSmFVTk1VaXR2U2xwaFdVMHJSRVpZT0hncmJXTkpiV3B1U2k5WFkzZ3lka1V5UTNvNVZYcFRRekEyVlhZek16UjRRVzAwYURGR1IweERXRGRPT0VSaUx6UmxRMVIyTWl0VGQwSXlaM1E0TlZrek1GWnBUMFp6TVZvNE9EWnlUbWhtUTFkblpVWnBkRlJJVTBSWFNGbDZVM3BzTlhwc05VUkVjbWhpYTFGSFREZFRNa3B2WTJOeFkyNXBZbUphYkN0c1JISnFNREZUTWpNMVVpdEpXRW81YVVGT1dXOUxRVEpFWm5ORVlVMHdVM0JSVW0wdldEUXhiMU5rTDJWSFUyOWtSbTVNVUhaaVVqUkJVVWRIUTNSQ1ZFaGtXRzg0VVdwQmJIWnlVR041T1ZoaGRHTlZNWE53UmtkNFNHUmxUMGxNVVZsQlZHZDBVMWRVYzJkeU9DOVNPVXBvTmxKQlEwSndPV1poZDNWemVVcEdjbmgyTjAxVFoxUldkVFpUYUVOVmVqZFJkbVJVTnl0clJ6SjVjbmQ0U1c1VU4wdFFZU3NyUm1NM0wxTXdXRzFwVlVaNVpVdGhOSHBzUVhNMUwza3pXVE0zTVN0UFYwMDFlVzlpZGpOalpVRTRZbXR0ZVdreVVXNVNWblppTWpGaFRGSkRRU3RTY1Vab05uWnRjV1pDTm1WSVYyZGFjMk5PZUZGa1JEZEdiR2hSZFhCUllWZGhkMGhGZEdaelJ6ZEliRkJLUkVOUlZIcDVPVk5TUldOU1ZXMXlTWEppVVZkMmEwdGFXalZUZEZGdWFtSlhiRE5ZWkhwVmN6VkVaMWt5TTA0dlMyUlRZbVV5VGtSMksxZFJOMmxVUVZsVE1HSlBkVVZRTnpOU1oybHJUa01yUjNKNVVsSlhXbmhFT1U1UFEzTk1jRFZxUmtGMWRubHNVbGczYTFka2RURmhiVXRDY1ZNeldEaGtZV0Y1Ynl0V2NVNXBka2hqT0RKRmQxTnlhVmhGZFVabE9UQkpiVzF3YkdJelF6Rm5OV1UyYjA1aGRtUnZZemRETjJoS09HYzRVMGt5T0dOdlNpdDZPRTVVVjBobmVtaE9SM2hRYjFCVFRtbHBNVGh4VDB4WlRUVTJlRE0xWkdSallrOUhjakkwUTNKVWIwa3lTbGhxSzBka1JWZFRaaXQ0ZVZKa1EwOHpTMUZFVkUwM09WaGlOa0l2ZUUxd1FsaFBhUzlWVEhOVE5HUXZWbmhQYUVkc1JrNHdLMjB2Tlc1U09GWktVazV0Y0ZCcmExcFZXRTlRYXlzNWMwWk5aMHRzTTJKTmMyWlNhRFZ6ZUV0bFdESlhkekp2VGxNMWJqVnFZMDlFWlRZNVZUZDFTRzFPVmpjcmRrRkRZVTVRVVhkNUwxSnpTbTU2YjNjME9WTkJRbmxVSzNoalMxUTNPRE5ZWVU5b1R5czFjbUYyVWtKSllXbHNkVnBhTVhFeWJFaHdhVVJyUVRneVJHbFlhVXgzYTBSMmRXUXhjbnBYVnpRemJ6aFhTMDFCYm5OcEwycDZTbTVrVEROUUwwNXVjV0kxUmpjdk9XUm1OVGR1WlZGemNFdEdkVWhHY3pod2FuVXhiRXBtVUcxRVFrZHFRa1pwYmtKcWFWa3dXWGxHTVdreWJGRm9NaTlIVFRnME9XODFZelJuU0VKc05VWmxjMEZyTDBkdVMzQlZSVzVVZUZOcWFubEtiRUl3UjNaRFRrSm5UWEZaYWpSU1RtZG1MMU1yT1dKUWNsQlZNa3RDVDBGUk4ybDNSMWh1WW5OcFpVSmpOV3BJTUZkRVpHUmhRWFpzUkhsWk1WWkthMlZ3VGpOTEwzZEdTV1U1V1dzeWJWRklkRTQwSzNJMFZuSklhWFJRUWpkMWJtaDBURGxzVGxNNGFreFliRWhzZFV4UVVUWnJiVzR3VVVOS1dqQlNXSFpHV0hkVFFqRk5TMkkzTWxOclJYWXhURGhUY2xacVRYWmtWbTk1VlRkUGNHbGlUVVp3WVhaMVNVbE9aVTEzVUZwR01qWlVVR1puV21nM2QxUkhZMWh2UTNwNVZHRmFaeXR5UVhsNlNtbDNiRGR1YUhWMmQweElhMm9yY1U5dGVVWTJjRVZoVGpnMFpteHBOWGh0WXk5TUswMVpiM0ZSV0ZkbFJXOVBZazFJTkVwMGNHOTJibEJLYWpJM1dGTlhaMmRqYm5sUU56UXJTRGMwVUVWR1ZuSTNabkV2T1ZKVGFDdElaVFF4U21JM04yTlhObmwwVURaT1MyMXdNM3BSU0RkcWFHRlhVRkU1ZG1wYUwwWlVVblV2U0VsUU1HUjFhR1UxTW1WcGJHSlpjbWRwZW1KeVF6aDNUVTFWZEZOUWFFUmtibTFGZGpBeVp6VlNlbWd6TVRoQlJrcHlZaXRyVFZSNFp6UkhNMDkzUlZCUk9IQnBiRUZPTm5kMWRrcDVVRTh5V21WSGEydE1kV00yVWxCR2FTdGlSelJRWlVsNk1FOVlhbGRuYW5keGMyOHZReTlXVFRKSFpWaGFVMVJrZEZSSFEwWjNSbEJzV21aTGFVRlJjekp0UTJGUmFtVTVZalZGVkhoS1RVMXhZMGRRZFhZMlEwMWxRMEo0VFZVM1JVZEVabk00YVVvM1YydEJPREZHZGxoTWFGazVlRlpwVGxsMmRTOUVaamhXVlUxd1VsaGpaa2xtUm5SeWVVVXpWR2h1V0RNeWNGVkRhR3gzZEdKc1J5ODJTVUZzVVhWYUwzazRlRFJXTlZKaGNuZE5WR1Z4U0dScU9FTnFTVVZPWkROTlQzbEhXRVZrV2l0Qk1EWjBSR3RQVG1KNGVuUmxRWEpCVVdGakx6aHZNMVprUTNneVEwbFFkbG93VGtoV1J5OVBhVzF3YjNWYWREVkZUazF3YkhsS1UxRllhMUpvUlUwclJsSnRiVUZqYmpSSlFYYzBPSGxuY1dSaGQweDZTVzlzWnpoVFpGUTNhVkJXVldkUlltRTBiMlZHUVhjM1FtNXVUVEJ4S3pNME56Tk5aemRaYkZSUE9ISldNR3NySzFwT2JsaEZWSFJ6U1ZSWlRHOTJVRGhTT0hKQ1UxVnZXR1psTlZOSlYyNWxPR1pqUFEuZVdxTURqeHhHMnNBVHVwdEVqUGJ3VmtrbVZPdTV5QjVORkhWYnpqOW1UM3Bza0lDdXJ1SHVrbFJvb2dpTFJkLWV1dVNyejRLVEthdFlVMnB0NV9xcVE=");
        params.put("_eventId", "submit");
        params.put("_llqmc", "Chrome");
        params.put("_llqbb", "79.0.3945.88");
        params.put("_czxt", "Windows");
        params.put("_czxtbb", "Windows 7");
        params.put("_llqxx", "mozilla/5.0 (windows nt 6.1; win64; x64) applewebkit/537.36 (khtml, like gecko) chrome/79.0.3945.88 safari/537.36");
        params.put("loginType", "0");
        params.put("checkType", "check_password");
        params.put("scene", "login");
        params.put("sjly", "0");
        params.put("codeType", "Y");
        params.put("authencationHandler", "UsernamePasswordAuthencationHandler");
        return params;
    }

    /**
     * 从电子税局抓取国税地税信息
     * 1.模拟访问登录页面
     * 2.通过用户名、密码、验证码登录
     * 3.检查登录结果,如果成功,调用checkLoginState以获取纳税人信息
     * 4.从返回的纳税人信息中匹配客户名称,然后调用changeQySf.do切换身份
     * 5.调用URL_fetch地址抓取税务信息
     *
     * @param customerName        customer name
     * @param legalPersonAccount  法人账号
     * @param legalPersonPassword 法人密码
     * @param validCode           验证码
     * @param kd                  httpclientutil
     * @return 税局同步信息
     */
    public static R syncTaxInfo(String customerName, String legalPersonAccount, String legalPersonPassword, String validCode,String csessionid ,String sig , String token ,String scene, HttpClientUtilKA kd) {
        String url = URL_login + System.currentTimeMillis();

        //1.模拟访问登录页面
        Map<String, String> params = constructParams();
        params.put("userName", legalPersonAccount);
        //对密码进行加密
        Encoder encoder = Base64.getEncoder();
        params.put("passWord", encoder.encodeToString(legalPersonPassword.getBytes()));
        params.put("captchCode", validCode);
        //阿里云滑动验证码返回csessionid会话值
        params.put("csessionid", csessionid);
        //阿里云滑动验证码返回sig签名值
        params.put("sig", sig);
        //阿里云滑动验证码请求token值
        params.put("token", token);

        //2.通过用户名、密码、验证码登录
        logger.info("调用登录，url={}", url);
        String res = kd.doGet(url, params);
        //CookieHolder.init();
        //String res = doGet(url, params);
        logger.info("调用登录，返回结果={}", res);
        return parseByLogin(customerName, res, kd);
    }

    /**
     * 3.检查登录结果,如果成功,调用checkLoginState以获取纳税人信息
     */
    private static R parseByLogin(String customerName, String res, HttpClientUtilKA kd) {
        Parser parser;
        try {
            parser = new Parser(res);
            TagNameFilter filter = new TagNameFilter("body");
            NodeList nodes = parser.extractAllNodesThatMatch(filter);
            String body = null;
            if (nodes != null) {
                for (int i = 0; i < nodes.size(); i++) {
                    Node textnode = nodes.elementAt(i);
                    body = textnode.toPlainTextString();
                }
            }
            if (body == null) {
                return R.error("解析登录返回信息错误");
            } else {
                if (body.contains("CAS登录成功")) {
                    logger.info("CAS登录成功");
                    logger.info("调用checkLoginState");
                    String checkLoginRes = kd.doGet(URL_check_login, null);
                    //String checkLoginRes = doGet(URL_check_login, null);
                    logger.info("调用checkLoginState返回={}", checkLoginRes);
                    return changeQySf(customerName, checkLoginRes, kd);
                } else {
                    return R.error("登录失败");
                }
            }
        } catch (ParserException e) {
            logger.error("解析登录返回信息错误", e);
            return R.error("解析登录返回信息错误");
        }
    }

    //4.从返回的纳税人信息中匹配客户名称,然后调用changeQySf.do切换身份
    private static R changeQySf(String customerName, String checkLoginRes, HttpClientUtilKA kd) {
        JSONObject obj = (JSONObject) JSONObject.parse(checkLoginRes);

        String flag = obj.get("flag").toString();
        if ("ok".equals(flag)) {
            logger.info("调用checkLoginState成功");
            JSONArray nsrQysqVos = (JSONArray) obj.get("nsrQysqVos");
            //切换身份时需要用到的字段
            String qybdid = null;
            String gsnsrsbh = null;//纳税人识别号
            for (Object nsrQysqVo : nsrQysqVos) {
                JSONObject obj1 = (JSONObject) nsrQysqVo;
                String zzNsrmc = obj1.getString("zzNsrmc");
                if (customerName.equals(zzNsrmc)) {
                    qybdid = obj1.getString("qybdid");
                    gsnsrsbh = obj1.getString("gsnsrsbh");
                    logger.info("从checkLoginState返回信息中匹配纳税人身份成功,纳税人名称={},qybdid={},gsnsrsbh={}", zzNsrmc, qybdid, gsnsrsbh);
                    break;
                }
            }
            if (qybdid != null) {
                Map<String, String> params = new HashMap<>();
                params.put("qybdid", qybdid);
                logger.info("调用changeQySf,url={},param={}", URL_changeQySf, params);
                String changeQySfRes = kd.doGet(URL_changeQySf, params);
                //String changeQySfRes = doGet(URL_changeQySf, params);
                logger.info("调用changeQySf,返回={}", changeQySfRes);
                obj = JSONObject.parseObject(changeQySfRes);
                if ("error".equals(obj.get("flag"))) {
                    return R.error("调用changeQySf返回错误");
                } else {
                    return fetchNationalTaxInfo(kd, checkLoginRes, gsnsrsbh);
                }
            } else {
                return R.error("从checkLoginState返回信息中匹配纳税人身份失败");
            }
        } else {
            return R.error("调用checkLoginState返回失败,flag=" + flag);
        }
    }

    /**
     * 抓取国税和地税的参数信息,类似于：
     * bw:{"taxML":{"head":{"gid":"311085A116185FEFE053C2000A0A5B63","sid":"yhscx.swdjcx.nsrjcxx","tid":" ","version":""},
     * "body":{,"gdlxbz":"GS","nsrsbh":"44010057995059X"}}}
     *
     * @param gdlxbz GS或者DS
     * @param nsrsbh 纳税人识别号
     * @return map
     */
    private static Map<String, Object> buildFetchParams(String gdlxbz, String nsrsbh, String sid) {
        Map<String, String> head = new LinkedHashMap<>();
        head.put("gid", "311085A116185FEFE053C2000A0A5B63");
        head.put("sid", sid);
        head.put("tid", "+");
        head.put("version", "");

        Map<String, String> body = new LinkedHashMap<>();
        if (StringUtils.isNotBlank(gdlxbz)) {
            body.put("gdlxbz", gdlxbz);
        }
        if (StringUtils.isNotBlank(nsrsbh)) {
            body.put("nsrsbh", nsrsbh);
        }

        Map<String, Object> taxML = new LinkedHashMap<>();
        taxML.put("head", head);
        taxML.put("body", body);

        Map<String, Object> bw = new HashMap<>();
        bw.put("taxML", taxML);

        return bw;
    }

    private static String buildFetchUrl(String gdlxbz, String nsrsbh, String sid) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        sb.append(URL_fetch).append("?t=").append(System.currentTimeMillis());
        //构建bw信息
        Map<String, Object> bwMap = buildFetchParams(gdlxbz, nsrsbh, sid);
        String bw = JSON.toJSONString(bwMap);
        //对特殊字符进行urlencoder
        bw = bw.replaceAll("\\{", URLEncoder.encode("{", "utf-8"));
        bw = bw.replaceAll("}", URLEncoder.encode("}", "utf-8"));
        bw = bw.replaceAll("\"", URLEncoder.encode("\"", "utf-8"));
        sb.append("&bw=").append(bw);
        return sb.toString();
    }

    /**
     * 抓取国税信息
     *
     * @param kd http client utils
     * @return r
     */
    private static R fetchNationalTaxInfo(HttpClientUtilKA kd, String checkLoginRes, String gsnsrsbh) {
//		Map<String, String> params = new HashMap<>();
//		params.put("t", String.valueOf(System.currentTimeMillis()));
//
//		//存款账户账号报告查询
//		params.put("bw", "{\"taxML\":{\"head\":{\"gid\":\"311085A116185FEFE053C2000A0A5B63\",\"sid\":\"yhscx.swdjcx.ckzhzhbg\",\"tid\":\" \",\"version\":\"\"},\"body\":{}}}");
        String url;
        try {
            //抓取国税信息
            //url = URL_fetch+"?t="+System.currentTimeMillis()+"&bw=%7B%22taxML%22:%7B%22head%22:%7B%22gid%22:%22311085A116185FEFE053C2000A0A5B63%22,%22sid%22:%22yhscx.swdjcx.nsrjcxx%22,%22tid%22:%22+%22,%22version%22:%22%22%7D,%22body%22:%7B,%22gdlxbz%22:%22GS%22%7D%7D%7D";
            url = buildFetchUrl("GS", gsnsrsbh, "yhscx.swdjcx.nsrjcxx");
            String gsres = kd.doGet(url, null);
            logger.info("抓取国税信息，返回结果={}", gsres);
            //抓取地税信息
            //url = URL_fetch+"?t="+System.currentTimeMillis()+"&bw=%7B%22taxML%22:%7B%22head%22:%7B%22gid%22:%22311085A116185FEFE053C2000A0A5B63%22,%22sid%22:%22yhscx.swdjcx.nsrjcxx%22,%22tid%22:%22+%22,%22version%22:%22%22%7D,%22body%22:%7B,%22gdlxbz%22:%22DS%22%7D%7D%7D";
            String dsres = null ;
            try {
                url = buildFetchUrl("DS", gsnsrsbh, "yhscx.swdjcx.nsrjcxx");
                dsres = kd.doGet(url, null);
                logger.info("抓取地税信息，返回结果={}", dsres);
            }catch (Exception e){
                logger.error("抓取地税信息，请求报错：{}",e );
            }


            //抓取购票员信息
            url = URL_queryGpycx;
            String gpyxx = kd.doPostJson(url, "{\"start\": 0, \"limit\": 10}");
//			MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//			formData.add("start", "0");
//			formData.add("limit", "10");
//			String gpyxx = doPost(url, formData);
            logger.info("抓取购票员信息，返回结果={}", gpyxx);

            //存款账户账号报告
            url = buildFetchUrl(null, null, "yhscx.swdjcx.ckzhzhbg");
            String ckzhzhbg = kd.doGet(url, null);
            logger.info("抓取存款账户账号报告，返回结果={}", ckzhzhbg);
            return parseGsDs(gsres, dsres, gpyxx, checkLoginRes, ckzhzhbg);
        } catch (Exception e) {
            logger.error("同步国税信息报错：{}", e);
            return R.error(e.getMessage());
        }
    }

    //解析国税和地税的返回结果
    private static R parseGsDs(String gsres, String dsres, String gpyxx, String checkLoginRes, String ckzhzhbg) {
        CustomerTax customerTax = new CustomerTax();
        try {
            //国税信息
            JSONObject gs = JSONObject.parseObject(gsres);
            JSONObject taxML = gs.getJSONObject("taxML").getJSONObject("body").getJSONObject("taxML");
            JSONObject bgcxswdjb = taxML.getJSONObject("bgcxswdjb");
            JSONObject djztxx = bgcxswdjb.getJSONObject("djztxx");
            JSONObject sgyxxVO = bgcxswdjb.getJSONObject("sgyxxVO");
            customerTax.setBsrxm(djztxx.getString("bsrxm"));                        //办税人姓名
            customerTax.setBsryddh(djztxx.getString("bsryddh"));                    //办税人电话
            customerTax.setBsrzjhm(djztxx.getString("bsrzjhm"));                    //办税人证件号码
            customerTax.setCwfzrxm(djztxx.getString("cwfzrxm"));                        //财务负责人姓名
            customerTax.setCwfzryddh(djztxx.getString("cwfzryddh"));                    //财务负责人电话
            customerTax.setCwfzrzjhm(djztxx.getString("cwfzrzjhm"));                    //财务负责人证件号码
            customerTax.setFddbrxm(djztxx.getString("fddbrxm"));                        //法定代表人姓名
            customerTax.setFddbryddh(djztxx.getString("fddbryddh"));                    //法定代表人电话
            customerTax.setFddbrzjhm(djztxx.getString("fddbrzjhm"));                    //法定代表人证件号码

            customerTax.setCheckLoginState(checkLoginRes);

            JSONObject bgcxDJSwdjbxxVO = bgcxswdjb.getJSONObject("bgcxDJSwdjbxxVO");
            customerTax.setNationalTaxNumber(bgcxDJSwdjbxxVO.getString("nsrsbh"));        //纳税人识别号（社会信用代码）
            JSONObject qtxx = bgcxswdjb.getJSONObject("qtxx");
            customerTax.setNationalTaxDpt(qtxx.getString("gszgsws"));

            customerTax.setSwryxm(sgyxxVO.getString("swryxm"));

            //投资方信息
            JSONObject tzxxlist = bgcxswdjb.getJSONObject("tzxxlist");
            if (tzxxlist != null) {
                Object tzxxObj = tzxxlist.get("tzxx");
                if (tzxxObj instanceof JSONArray) {
                    JSONArray tzxx = (JSONArray) tzxxObj;
                    List<String> list = new ArrayList<>();
                    for (Object o : tzxx) {
                        JSONObject jo = (JSONObject) o;
                        list.add(jo.get("tzfmc").toString());
                    }
                    customerTax.setInvestorInfo(StringUtils.join(list, ","));
                } else if (tzxxObj instanceof JSONObject) {
                    customerTax.setInvestorInfo(((JSONObject) tzxxObj).getString("tzfmc"));
                }
            }
            if (StringUtils.isNotBlank(dsres)){
                //地税信息
                JSONObject ds = JSONObject.parseObject(dsres);
                taxML = ds.getJSONObject("taxML").getJSONObject("body").getJSONObject("taxML");

                bgcxswdjb = taxML.getJSONObject("bgcxswdjb");
                if (null != bgcxswdjb){
                    qtxx = bgcxswdjb.getJSONObject("qtxx");
                    customerTax.setLocalTaxDpt(qtxx.getString("dszgsws"));
                }
            }



            //购票员信息
            JSONObject gpyjs = JSONObject.parseObject(gpyxx);
            JSONArray gprarr = gpyjs.getJSONArray("data");
            List<String> gprList = new ArrayList<>();
            if (gprarr != null && !gprarr.isEmpty()) {
                for (Object aGprarr : gprarr) {
                    JSONObject jo = (JSONObject) aGprarr;
                    gprList.add(jo.getString("gprxm"));
                }
                customerTax.setTicketAgent(StringUtils.join(gprList, ","));
            }

            //存款账户账号报告
            JSONObject ckzhzh = JSONObject.parseObject(ckzhzhbg);
            taxML = ckzhzh.getJSONObject("taxML").getJSONObject("body").getJSONObject("taxML");
            JSONArray ckzhzhbgGridlb = taxML.getJSONObject("ckzhzhbgGrid").getJSONArray("ckzhzhbgGridlb");
//			if(logger.isInfoEnabled()) {
//				logger.info("存款账户账号报告={}", ckzhzhbgGridlb.toString());
//			}
            customerTax.setCkzhzh(ckzhzhbgGridlb.toJSONString());

            return R.ok().put("customerTax", customerTax);
        } catch (Exception e) {
            logger.error("解析国税地税信息出错", e);
            return R.error("解析国税地税信息出错");
        }
        //return R.ok().put("gs", gsres).put("ds", dsres);
    }

    public static void main(String[] args) throws Exception {
        String url = "http://www.etax-gd.gov.cn/web-tycx/tycx/query.do?t=1507544744858&bw={\"taxML\":{\"head\":{\"gid\":\"311085A116185FEFE053C2000A0A5B63\",\"sid\":\"yhscx.swdjcx.nsrjcxx\",\"tid\":\" \",\"version\":\"\"},\"body\":{,\"gdlxbz\":\"GS\"}}}";
        String encodedUrl = URLEncoder.encode(url, "utf-8");
        System.out.println("encodedUrl=" + encodedUrl);
        String decodedUrl = URLDecoder.decode(url, "utf-8");
        System.out.println("decodedUrl=" + decodedUrl);

        System.out.println(URLEncoder.encode("bw:{\"taxML\":{\"head\":{\"gid\":\"311085A116185FEFE053C2000A0A5B63\",\"sid\":\"yhscx.swdjcx.nsrjcxx\",\"tid\":\" \",\"version\":\"\"},\"body\":{,\"gdlxbz\":\"GS\",\"nsrsbh\":\"44010057995059X\"}}}", "utf-8"));
        System.out.println(buildFetchParams("GS", "44010057995059X", "yhscx.swdjcx.nsrjcxx"));
        System.out.println(buildFetchUrl("GS", "44010057995059X", "yhscx.swdjcx.nsrjcxx"));
    }
}

package com.zdb.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdb.modules.customer.entity.CustomerTax;
import org.apache.commons.lang.StringUtils;
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
        params.put("lt", "LT-546681-BFExfWhQEJjMWfIdx2Tw-gddzswj");
        params.put("execution", "e01c22c7-e97c-4998-98c5-201b4ba51417_ZXlKaGJHY2lPaUpJVXpVeE1pSjkuYW1RcmFIUTJOMUFyVmpJME1UaG9Vek5IZVVoS1pESlBVRTlFZFRsdlZsSldlRm8zU0VOSlJURXZTR0oyWjBGeE5uaEVZVlprT1hsWlRFSlNjbHBJYWxrMFVYaHFRa3AzYTNWU0wyWlNhazVaU1VrNVNsQkRXWFo0YWpOWFVYTXdaM2RQYmxCaFYxWnhORzFtY214cVltbE9jMGxUZFdrMlpVUTBjMVpLTDA1YVNHZG5RUzlEYWk5a05IcGpWVEZ5YURWcGRHaG5PRGRsVkhGVU5qSllURVV5ZHpBeU0wSXliMmM1YzFGbVpsbG5PWEJrTnpCSUx6WlBNMnhUWW5oWmNGaHpSMGRDYkc1UFFWaEZabGhzWVRnMWNHRlJiR3hpYlVGTVRsaHJZMWhYTlM4eVdVOVNRbmRaZWs1WFNXeFJhbE01ZW14U1NIVTBTRWt6V0VNMVdIUjBWaTl0WnpOSlRrWkRhbHBDWkhORE5VUllhUzkyTVN0d1dVZEZXRWgySzBsMWIxUktPRzlQYW5oRFMwTnNNMmw1Y1ZoRmVqSjFOWEl2UkRsU1YwSk9USFpuUVUxamVqSTFaRmxuVlN0SWVFWndTWE0yVFZCRlV6SkdLMVJGUlVoeGVGSk9XQ3R0U25SeGVreFVOMnN4VUdnckszQjBkbW93UlhsMWJHNDBkVVpOZEZkTWVrRkpVamxwVUZKWlVtMU5UMFZGY25sU2RVUkRXRmw0V2tOU2VFTnFNazV0U2tWS1RTdFBZUzhyVUdGck9FbzRMMjF3UTBaSGVXaFZPRUZVYWk5RVVFbHRhWFZ1TVdRcldtOW9VREZ6Ums1VU1EZDBTMlY0TUdSbkx6SkpjREZEYUdsbFZWbFViemt3WlV0MVkxWlJTamt2VFU1T1dEZ3dMMVZwVGxkck16TkxTekJ3VlZNMkszUXpXRzlXT0RGaWFIVjRWM2hHU2pCQ1RUTXhOVU5uYlcxc1pVSjRNVkZET1ZwNEt5dElUVUV6V0VoTVpubHNhWEJvZEcwNVRrTXJZMjkxYVc0elNFODBaRFUwTXk4emNXSk9NbXhEYVNzdk5XUmhibUZPT1RsV1NYcENNMEZWUzNKaGRWVTNXRVJNTW5OcGRERndVbEV5Y0hoNE5URjZWR3RzTkVGYVZFVmtkRWhDTDJSa1RXeERlRlpxVXpOaWFXUnBUWFUyVUVWamNHWm9MMnBYU0dOalVHZDVNVWgwVVM5MGIxSkViV0pUY0ZCaGF6WnhjblEzVURSNFRFa3JkWGxwWTJWNlZWTnNNMUJzUjBoRFIxUXJiVTg0YlhSUlFsZDRVRTVZTDNWd1EwZE5LMUZaYUdGdmNHdzROMWt6TW5CRVJGbDRXREZDVDNkS0syeEhXVVJJTm1wc1JWWkxkVGw2ZVhCSWJFeDZkR2tyY1VkbU0xbzRaa1l3ZERKTFdraDZjelZuTkdGaGREWnRjalZhZW1rMk9HMUdWRFZQVEhoWVZFZFNjRmxVVkZVM1RTOXRRMFV2Y1d4RmFFbHlXRk5tWWpGS1ptazRjRTFKTDBGaGRreFFlV2R2Vm1SWE5sZDJWa0ZHZVZSTWJuaFhaVXd5YjFBNFZsQjJXWEZDWVVSU1NGTTFSVEZ4WTFvMGNIVllkR0V3ZDJrNFEyRlpUMnBsWjBNNVFYWjZjMVpVYnpOcFJtUkhSM1ZUYmpNemRXVm5RMjFCTWs5QlpXcG1jR3R5VUhOTVZ6azNPRmQ2YzNKWWJrNXFhMjVZWWs4M1owcFJOWFZ3WTBSMmJsa3ZiM1o1ZDA1Mk1VeFBhbWxuYkdscUszVlhTQ3RQZVRsRGRrWTFXV1ZZWjJKU05EUlZSbEl6WTFSeWVrdzBjazlXVnpkUVVqWmxaM0puYlRCVGJpOHJSbWN3YkRKMlpFUXZUemRKVkRJclpGZHlNbVF4U0ZGeFlUWkRjRGhGVjFvclFXMTBTa05KVm0wM1Iwd3JZVGREVUZvMWVUaHVXRkJrWjJaT1MzZExObk5HZW01T1RISXlXVWgwYjJKV04zZE1UelEyWW5STmRGQTJNbkYzVldGeFYwdEtXRVV4YVRWdFZUUXJTRXhRVURSSlpVMHdaRlp3VTNGd2VsTnljelpoVW5sMFFUaGplbFJuUjJoTWRXMWxWRXhqZUZVNVMwcE5ZMk5LVUVrek5WVlpjWGRYUkUxWWVsRTRVRUpsYTA1aVQxZDFaVk5MZFc5S1pqUlBTR014ZGxWbGJFNVhVMk5pUTFKNWNWVlpiRmxLY1VkaE0xaFRkSGRCWjNCa1dETkJSR3B4TTJjNFVXZzNPWEZCZDFSQmJFVlZTalpEWlhKTE4xUnRXVUYxV0RjMlQwZFlVMUpIUzBjeGNWUlBWV2hqZVRWelR6WkRPRWx6TDAwclZXTXhTbFJhVVhOblRXZHdNRnBWTUZsSmJIaFdjSFkwTW01RVYxRnpjMGgyTTFsc1dtcFFiVE4xZWtKNFlYcHhjQzlGUjBOeE9GcHJkbW9yVjJSUWF6TXdiMmRrY21kMVkwOWlkMVpoU0ZCd2VtNWtVbEJhTTFnelpXOXJZVzlrYXpSdGJFcFVSSFl5Tm1aWlkxZEJRMlZGTUZwd1puUnViVlF4VVUxRVozaENRVVo2VGxOVWVVMU9hMlZYVnl0ak9EZDFOVXRUVUU5TFpFWk1SbFl2VVdnMWFHd3dlbWRTWTFWTVluRTBTRXRtYTBGd09IZHBVRFUyU1hCalJYaFdSMUprZFVSbGQySlhLemRCTlUxeFNrTllPVzhyU0ZGbFRXNTBZVGt4YkVaQlEyTTFNWEpsVVdsWFNuZHdWR0U0VTFOTGMwcEpTVU5vZERoclVUbDJSbGgxYlROV1dEQnZaVlZyUzFkMlZFZFZZbWczYkhWUVRISjZWU3R1UjJGNmQxZHFNbFpaVjJWSlFsbG9NVUpDV0d0NVkydFVObmxzTlV0WFZrcENjRXBJUzFGMWJHdDJaVmxGUzFveVoySnhkRVV4U0V4b1ZtSk9ibUZ0YldoeGIzSktlRlJKUzJJM1RuZGpZMEpCUVN0bWJEZDJNaXRUWjIxRE9UVlZiV3RQWVVwclMzQkJkMUZZYTJ4bGRXVlROa3B6T0hsdmEwWlNTSGgwWTBWV1JsbDNaV05zYVhwVldUTkViVE5MVGpKblNtNUtXRFV5ZDNwNlJuaFZOVEpTWW5OaVMzZHdiWEp3YkZjNFUyRnlVVFpaY21RME9YQkxlU3RXWkhoUWVtSTBTRkk1UzNoTWF6UlZaV3N2WlROSmJEZEhLMFpvUkZBelYyWm9iVkZ2WVVnME1rOU1lRW92Ym1Wck16bGhjRTlhWjBkalpIWjBSalVyY1hGa2MwTm5kV0ozYVU1SFpUTlVXa3hLUW10RmJWbzFSbTVDU2xCMVZUaG5jakF2VVZZMGJXMDNNbFlyWkRGQ1RFSXlOaXMyU1RGS2JVOW9hM1ZrY0VsT1JXRk1ZbnBVYWxsVWFrdHJVbEp1ZEc5ek0zTlZVM2szWjBaNFQyaFlaRmxKYmxORWNXUXpVMmhyV1VKNFJuUnZValJhUVdseFNtVkNOWFpMY1VReGIyTlZTemxFZVVwcGJGSmtNaXRtV1dRMmNGWm9UV1JQZHprd2NtWmpSa2xEYzFvMFdGVkNVbVZDVERSNFN6UlNOMFpKYjNKb1RFVnJZakJGUVVOcVFXTjZkRXh2ZFhSWmVIQkVieXRwYzFReFVWSlJUVU00TTJGalRqYzNRM2RuTUhkcFEwa3lZazEyVGtKbVpYb3ZRV3BFUkd3MFkxaHpRbVl2UzJoeFRHRnZSVVY0ZVhNclJGazVVa1Y0UVU5NVVGRkthRkZWVEVOR1NFOTJlV3AzZVVaWGRVdFdUMk5RTmpaak4yTlNRek5NTVRKS09VSXdXSE5IUzJsT2FXTlFZMm93ZVhGWGREQjRaSFZtTkdOdWFFc3dlRzU1TjBONFJWTlFZemwyTUVNeE5rczFSMUYxVlhkQ2ExQlRUMll4VTFKSlUxZENPWEJVUW1wYVIyNWtlRGR4UjB4MmFHeFphVmN3YkhkVGVEVnZRMXBRVGpVMFRGaFhTVmhwYldSc2VEVlBjRkpoYTJGS05HNWFiaThyWnpkdlQzVXdWVUpHU1hCSE1tSXdNVkJoV1VWM2NuUmhlVGx4SzFSVVJtcFBPV3BGV0hKblUwOVNZVUZzYVVwME56bEpRbnB6VURsb09USjJRM1YwY1RsbloyMTVXVU5DVWpkWlFWTkZha3BsY0N0Uk1FaDBlbk50WVhsaFZFOXdaVU5EVm1Ocll6QkhNVmN5U1hGMGFGQnlOSGRXVVdRd2RrMVRiM1Z4U0dKbmNreHNibTVIVW5sUVJHdEpiWFJIVUhkRmEycFlWVXQxYVVwcWJHdEhZbEZ3Y2tJeFpXdHdNa1JLWWtGbmEwSXhNbE5TTkRWMFJsQmFRek5DSzJocVdrODNka05TY21SVGNqTm9ZU3RXUTJKa1ZGTTFUQ3MwT1dKTE0zbDVibGhEZVc1MFdXdHBLMlJHVGtFd1l6TmxVQ3ROVGxab1JVVnlZa1EwUm5CS04yMTFSSFEyUzFWYWJWSlNUekJpTVN0d01raHRXSEZCTkRCWE5USXdWbnBsY0ZKbFFVUTJhMlkyWVdsdVFuTjBXbHBuV1hSV2FtNTBSRXd4TlROTVoyOXNUbm92VjNKWVJuQlNUVGhvWVZSWk4xSTRVV2x4U1ZBNFBRLjJ0ay1ZMXROSXJOZ2toNHRVVlVoSHh5RzNrSkJVMmkwd09uVEV2bEJxQlg3b3ZpNDBtQVk2N3RyM1BkMFBVSjlLYkk5OXBTdTNNZDBhdmVFdXdNVDVn");
        params.put("_eventId", "submit");
        params.put("_llqmc", "IE");
        params.put("_llqbb", "11.0");
        params.put("_czxt", "Windows");
        params.put("_czxtbb", "Windows 7");
        params.put("_llqxx", "mozilla/5.0 (windows nt 6.1; wow64; trident/7.0; slcc2; .net clr 2.0.50727; .net clr 3.5.30729; .net clr 3.0.30729; .net4.0c; rv:11.0) like gecko");
        params.put("loginType", "0");
        params.put("checkType", "check_password");
        params.put("scene", "login");
        params.put("sjly", "0");
        params.put("authencationHandler", "UsernamePasswordAuthencationHandler");
        //params.put("codeType", "Y");
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
    public static R syncTaxInfo(String customerName, String legalPersonAccount, String legalPersonPassword, String validCode, HttpClientUtilKA kd) {
        String url = URL_login + System.currentTimeMillis();

        //1.模拟访问登录页面
        Map<String, String> params = constructParams();
        params.put("userName", legalPersonAccount);
        //对密码进行加密
        Encoder encoder = Base64.getEncoder();
        params.put("passWord", encoder.encodeToString(legalPersonPassword.getBytes()));
        params.put("captchCode", validCode);

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

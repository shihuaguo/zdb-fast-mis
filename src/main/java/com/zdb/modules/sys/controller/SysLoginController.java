package com.zdb.modules.sys.controller;

import com.alibaba.fastjson.JSONObject;
import com.zdb.common.utils.R;
import com.zdb.common.utils.WxUtil;
import com.zdb.modules.sys.entity.SysUserEntity;
import com.zdb.modules.sys.service.SysUserService;
import com.zdb.modules.sys.service.SysUserTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 登录相关
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年11月10日 下午1:15:31
 */
@Slf4j
@RestController
public class SysLoginController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserTokenService sysUserTokenService;

    @Value("${weixin.appId}")
    private String appId;
    @Value("${weixin.appSecret}")
    private String appSecret;

    private R checkUser(SysUserEntity user, String password){
        //账号不存在、密码错误
        if (user == null || !user.getPassword().equals(new Sha256Hash(password, user.getSalt()).toHex())) {
            return R.error("账号或密码不正确");
        }
        //账号锁定
        if (user.getStatus() == 0) {
            return R.error("账号已被锁定,请联系管理员");
        }
        return R.ok();
    }
    /**
     * 登录
     */
    @RequestMapping(value = "/sys/login", method = RequestMethod.POST)
    public R login(String username, String password, String js_code) {
        log.info("收到登录请求,username={},js_code={}", username, js_code);
        if(StringUtils.isNotBlank(js_code)){
            return login_wx(username, password, js_code);
        }
        //用户信息
        SysUserEntity user = sysUserService.queryByUserName(username);

        R r = checkUser(user, password);
        if((Integer)r.get("code") != 0){
            return r;
        }

        //生成token，并保存到数据库
        return sysUserTokenService.createToken(user.getUserId());
    }

    //通过微信登录
    private R login_wx(String username, String password, String js_code){
        SysUserEntity user = null;
        if(StringUtils.isNotBlank(username)){
            //用户信息
            user = sysUserService.queryByUserName(username);
            R r = checkUser(user, password);
            if((Integer)r.get("code") != 0){
                return r;
            }
        }
        JSONObject jo = WxUtil.queryUserByJsCode(appId, appSecret, js_code);
        if(jo == null){
            return R.error("js_code错误");
        }
        String session_key = jo.getString("session_key");
        String openid = jo.getString("openid");
        if(StringUtils.isBlank(session_key) || StringUtils.isBlank(openid)){
            return R.error("调用微信API错误");
        }

        //如果没有输入用户名,采用静默方式登录
        if(StringUtils.isBlank(username)){
            user = sysUserService.queryByWxOpenId(openid);
            if(user == null || StringUtils.isBlank(user.getWxOpenid())
                    || !Objects.equals(openid, user.getWxOpenid())){
                return R.error("用户信息不正确");
            }
        }else {
            user.setWxOpenid(openid);
            user.setWxSessionKey(session_key);
            user.setPassword(null);
            sysUserService.updateWxInfo(user);
        }
        return sysUserTokenService.createToken(user.getUserId());
    }

    /**
     * 退出
     */
    @RequestMapping(value = "/sys/logout", method = RequestMethod.POST)
    public R logout() {
        sysUserTokenService.logout(getUserId());
        return R.ok();
    }

}

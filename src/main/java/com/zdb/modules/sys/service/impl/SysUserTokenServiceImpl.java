package com.zdb.modules.sys.service.impl;

import com.zdb.modules.sys.dao.SysUserTokenDao;
import com.zdb.modules.sys.entity.SysUserTokenEntity;
import com.zdb.modules.sys.service.SysUserTokenService;
import com.zdb.common.utils.R;
import com.zdb.modules.sys.oauth2.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl implements SysUserTokenService {
    private final SysUserTokenDao sysUserTokenDao;
    //12小时后过期
    private final static int EXPIRE = 3600;

    @Autowired
    public SysUserTokenServiceImpl(SysUserTokenDao sysUserTokenDao) {
        this.sysUserTokenDao = sysUserTokenDao;
    }

    @Override
    public SysUserTokenEntity queryByUserId(Long userId) {
        return sysUserTokenDao.queryByUserId(userId);
    }

    @Override
    public void save(SysUserTokenEntity token) {
        sysUserTokenDao.save(token);
    }

    @Override
    public void update(SysUserTokenEntity token) {
        sysUserTokenDao.update(token);
    }

    @Override
    public R createToken(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
        //判断是否生成过token
        SysUserTokenEntity tokenEntity = queryByUserId(userId);
        if (tokenEntity == null) {
            tokenEntity = new SysUserTokenEntity();
            tokenEntity.setUserId(userId);
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);
            //保存token
            save(tokenEntity);
        } else {
            tokenEntity.setToken(token);
            tokenEntity.setUpdateTime(now);
            tokenEntity.setExpireTime(expireTime);

            //更新token
            update(tokenEntity);
        }

        return R.ok().put("token", token).put("expire", EXPIRE);
    }

    @Override
    public void logout(long userId) {
        //生成一个token
        String token = TokenGenerator.generateValue();

        //修改token
        SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(token);
        update(tokenEntity);
    }
}

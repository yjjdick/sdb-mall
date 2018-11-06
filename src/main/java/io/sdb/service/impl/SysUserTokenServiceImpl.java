package io.sdb.service.impl;

import io.sdb.common.utils.R;
import io.sdb.dao.SysUserTokenDao;
import io.sdb.model.SysUserToken;
import io.sdb.sys.oauth2.TokenGenerator;
import io.sdb.service.SysUserTokenService;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl extends BaseServiceImpl<SysUserTokenDao, SysUserToken> implements SysUserTokenService {

	//12小时后过期
	private final static int EXPIRE = 3600 * 12;


	@Override
	public R createToken(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token
		SysUserToken tokenEntity = this.dao.findById(userId);
		if(tokenEntity == null){
			tokenEntity = new SysUserToken();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			tokenEntity.save();
		}else{
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//更新token
			tokenEntity.update();
		}

		R r = R.ok().put("token", token).put("expire", EXPIRE);

		return r;
	}

	@Override
	public void logout(long userId) {
		//生成一个token
		String token = TokenGenerator.generateValue();

		//修改token
		SysUserToken tokenEntity = new SysUserToken();
		tokenEntity.setUserId(userId);
		tokenEntity.setToken(token);
		tokenEntity.update();
	}
}

package io.sdb.service;

import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import io.sdb.model.User;
import io.sdb.form.LoginForm;
import io.sdb.service.BaseService;

/**
 * 用户
 * 
 * @author dick
 */
public interface UserService extends BaseService<User> {

	User queryByMobile(String mobile);

	/**
	 * 用户登录
	 * @param form    登录表单
	 * @return        返回用户ID
	 */
	String login(LoginForm form);

	User addMaUser(WxMaUserInfo wxMaUserInfo);

	Integer getYestodayNewUsers();

	Integer getTotalUsers();
}

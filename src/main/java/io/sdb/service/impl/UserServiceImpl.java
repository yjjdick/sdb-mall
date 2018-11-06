package io.sdb.service.impl;


import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import cn.hutool.core.date.DateUtil;
import io.sdb.common.entity.Filter;
import io.sdb.common.exception.RRException;
import io.sdb.common.validator.Assert;
import io.sdb.dao.UserDao;
import io.sdb.enums.Language;
import io.sdb.enums.SnEnum;
import io.sdb.model.User;
import io.sdb.form.LoginForm;
import io.sdb.service.UserService;
import io.sdb.service.AreaService;
import io.sdb.service.SnService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<UserDao, User> implements UserService {

	@Autowired
	AreaService areaService;

	@Autowired
	private SnService snService;

	@Override
	public User queryByMobile(String mobile) {
		User user = new User();
		user.setMobile(mobile);
		return this.findFirstByModel(user);
	}

	@Override
	public String login(LoginForm form) {
		User user = queryByMobile(form.getMobile());
		Assert.isNull(user, "手机号或密码错误");

		//密码错误
		if(!user.getPassword().equals(DigestUtils.sha256Hex(form.getPassword()))){
			throw new RRException("手机号或密码错误");
		}

		return user.getUserId();
	}

	@Override
	public User addMaUser(WxMaUserInfo wxMaUserInfo) {
		User user = new User();
		String userId = snService.generate(SnEnum.USER);
		user.setUserId(userId);
		user.setAvatar(wxMaUserInfo.getAvatarUrl());
		user.setMaOpenId(wxMaUserInfo.getOpenId());
        user.setNickname(wxMaUserInfo.getNickName());
        user.setGender(Integer.parseInt(wxMaUserInfo.getGender()));
        user.setUnionId(wxMaUserInfo.getUnionId());
        Language language = Language.getByEnumName(wxMaUserInfo.getLanguage());
        user.setLanguage((int)language.getValue());
        user.save();
		return user;
	}

	@Override
	public Integer getYestodayNewUsers() {
		List<Filter> filterList = new ArrayList<>();

		Filter start = new Filter();
		start.setOperator(Filter.Operator.ge);
		start.setProperty("create_date");
		Date todayBegin = DateUtil.beginOfDay(new Date());
		todayBegin = DateUtil.offsetDay(todayBegin, -1);
		start.setValue(todayBegin);
		filterList.add(start);

		Filter end = new Filter();
		end.setOperator(Filter.Operator.le);
		end.setProperty("create_date");
		Date todayEnd = DateUtil.endOfDay(new Date());
		todayEnd = DateUtil.offsetDay(todayEnd, -1);
		end.setValue(todayEnd);
		filterList.add(end);

		List<User> userList = this.findByFilters(filterList);

		return userList.size();
	}

	@Override
	public Integer getTotalUsers() {
		List<User> users = this.findAll();
		return users.size();
	}
}

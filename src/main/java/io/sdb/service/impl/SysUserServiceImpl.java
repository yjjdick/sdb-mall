package io.sdb.service.impl;

import com.jfinal.plugin.activerecord.Page;
import io.sdb.common.annotation.JFinalTx;
import io.sdb.common.exception.RRException;
import io.sdb.common.utils.Constant;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.Query;
import io.sdb.common.entity.Filter;
import io.sdb.dao.SysUserDao;
import io.sdb.model.SysUser;
import io.sdb.service.SysRoleService;
import io.sdb.service.SysUserRoleService;
import io.sdb.service.SysUserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUserDao, SysUser> implements SysUserService {

	@Autowired
	private SysUserRoleService sysUserRoleService;
	@Autowired
	private SysRoleService sysRoleService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String username = (String)params.get("username");
		Long createUserId = (Long)params.get("createUserId");
		Query<SysUser> query = new Query<SysUser>(params);
		List<Filter> filters = new ArrayList<>();

		if(createUserId != null) {
            Filter filter = new Filter();
            filter.setProperty("create_user_id");
            filter.setValue(createUserId);
            filter.setOperator(Filter.Operator.eq);
            filters.add(filter);
        }

        if (!StringUtils.isBlank(username)) {
            Filter filter = new Filter();
            filter.setProperty("username");
            filter.setValue(username);
            filter.setOperator(Filter.Operator.like);
            filters.add(filter);
        }


		Page<SysUser> pr = this.paginate(query.getCurrPage(), query.getLimit(), filters, query.getOrder());

		return new PageUtils(pr);
	}

	@Override
	public List<String> queryAllPerms(Long userId) {
		return this.dao.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return this.dao.queryAllMenuId(userId);
	}

	@Override
	public SysUser queryByUserName(String username) {
		return this.dao.queryByUserName(username);
	}

	@Override
	@JFinalTx
	public void save(SysUser user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		user.save();
		
		//检查角色是否越权
		checkRole(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@JFinalTx
	public boolean update(SysUser user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.remove("password");
		}else{
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		boolean updateSucc = user.update();
		
		//检查角色是否越权
		checkRole(user);
		
		//保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());

		return updateSucc;
	}

    @Override
	public boolean updatePassword(Long userId, String password, String newPassword) {
		SysUser sysUser = new SysUser();
		sysUser.setPassword(newPassword);
		sysUser.setUserId(userId);
		sysUser.setPassword(password);
		boolean succ = sysUser.update();
		return succ;
	}
	
	/**
	 * 检查角色是否越权
	 */
	private void checkRole(SysUser user){
		if(user.getRoleIdList() == null || user.getRoleIdList().size() == 0){
			return;
		}
		//如果不是超级管理员，则需要判断用户的角色是否自己创建
		if(user.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}

		//查询用户创建的角色列表
		List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

		//判断是否越权
		if(!roleIdList.containsAll(user.getRoleIdList())){
			throw new RRException("新增用户所选角色，不是本人创建");
		}
	}
}

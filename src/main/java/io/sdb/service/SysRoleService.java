package io.sdb.service;

import io.sdb.common.utils.PageUtils;
import io.sdb.model.SysRole;
import io.sdb.service.BaseService;

import java.util.List;
import java.util.Map;


/**
 * 角色
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:42:52
 */
public interface SysRoleService extends BaseService<SysRole> {

	PageUtils queryPage(Map<String, Object> params);

	void save(SysRole role);

	boolean update(SysRole role);

	void deleteBatch(Long[] roleIds);

	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}

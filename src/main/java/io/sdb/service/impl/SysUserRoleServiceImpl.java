package io.sdb.service.impl;

import com.jfinal.plugin.activerecord.Db;
import io.sdb.common.annotation.JFinalTx;
import io.sdb.dao.SysUserRoleDao;
import io.sdb.model.SysUserRole;
import io.sdb.service.SysUserRoleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 用户与角色对应关系
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:45:48
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRoleDao, SysUserRole> implements SysUserRoleService {

	@Override
	@JFinalTx
	public void saveOrUpdate(Long userId, List<Long> roleIdList) {
		//先删除用户与角色关系
		SysUserRole delSysUserRole = new SysUserRole();
		delSysUserRole.setUserId(userId);
		this.deleteByModel(delSysUserRole);

		if(roleIdList == null || roleIdList.size() == 0){
			return ;
		}

		//保存用户与角色关系
		List<SysUserRole> list = new ArrayList<>(roleIdList.size());
		for(Long roleId : roleIdList){
			SysUserRole sysUserRole = new SysUserRole();
			sysUserRole.setUserId(userId);
			sysUserRole.setRoleId(roleId);

			list.add(sysUserRole);
		}

		Db.batchSave(list, list.size());
	}

	@Override
	public List<Long> queryRoleIdList(Long userId) {
		SysUserRole sysUserRole = new SysUserRole();
		sysUserRole.setUserId(userId);
		List<SysUserRole> sysUserRoleList = this.findByModel(sysUserRole);
		List<Long> roleIdList = sysUserRoleList.stream().map(item->{ return item.getRoleId(); }).collect(Collectors.toList());
		return roleIdList;
	}

}

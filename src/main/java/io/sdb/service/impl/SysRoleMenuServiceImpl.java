package io.sdb.service.impl;

import com.jfinal.plugin.activerecord.Db;
import io.sdb.common.annotation.JFinalTx;
import io.sdb.dao.SysRoleMenuDao;
import io.sdb.model.SysRoleMenu;
import io.sdb.service.SysRoleMenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 角色与菜单对应关系
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:44:35
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends BaseServiceImpl<SysRoleMenuDao, SysRoleMenu> implements SysRoleMenuService {

	@Override
	@JFinalTx
	public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
		//先删除角色与菜单关系
		deleteBatch(new Long[]{roleId});

		if(menuIdList.size() == 0){
			return ;
		}

		//保存角色与菜单关系
		List<SysRoleMenu> list = new ArrayList<>(menuIdList.size());
		for(Long menuId : menuIdList){
			SysRoleMenu sysRoleMenu = new SysRoleMenu();
			sysRoleMenu.setMenuId(menuId);
			sysRoleMenu.setRoleId(roleId);

			list.add(sysRoleMenu);
		}

		Db.batchSave(list, list.size());
	}

	@Override
	public List<Long> queryMenuIdList(Long roleId) {
		SysRoleMenu sysRoleMenu = new SysRoleMenu();
		sysRoleMenu.setRoleId(roleId);
		List<SysRoleMenu> sysRoleMenuList = this.dao.findByModel(sysRoleMenu);
		List<Long> menuIdList = sysRoleMenuList.stream().map(item -> {
			return item.getMenuId();
		}).collect(Collectors.toList());
		return menuIdList;
	}

	@Override
	public boolean deleteBatch(Long[] roleIds){
		return this.dao.deleteBatch(new String[]{"role_id"}, roleIds);
	}

}

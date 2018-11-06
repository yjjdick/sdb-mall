package io.sdb.service.impl;

import com.jfinal.plugin.activerecord.Page;
import io.sdb.common.annotation.JFinalTx;
import io.sdb.common.exception.RRException;
import io.sdb.common.utils.Constant;
import io.sdb.common.utils.PageUtils;
import io.sdb.common.utils.Query;
import io.sdb.common.entity.Filter;
import io.sdb.dao.SysRoleDao;
import io.sdb.model.SysRole;
import io.sdb.service.SysRoleMenuService;
import io.sdb.service.SysRoleService;
import io.sdb.service.SysUserRoleService;
import io.sdb.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:45:12
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleDao, SysRole> implements SysRoleService {

	@Autowired
	private SysRoleMenuService sysRoleMenuService;
	@Autowired
	private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String roleName = (String)params.get("roleName");
		Long createUserId = (Long)params.get("createUserId");

		List<Filter> filters = new ArrayList<>();

		if(createUserId != null) {
			Filter filter = new Filter();
			filter.setProperty("create_user_id");
			filter.setValue(createUserId);
			filter.setOperator(Filter.Operator.eq);
			filters.add(filter);
		}

		if (!StringUtils.isBlank(roleName)) {
			Filter filter = new Filter();
			filter.setProperty("role_name");
			filter.setValue(roleName);
			filter.setOperator(Filter.Operator.like);
			filters.add(filter);
		}

		Query<SysRole> query = new Query<SysRole>(params);
		Page<SysRole> pr = this.paginate(query.getCurrPage(), query.getLimit(), filters, query.getOrder());

		return new PageUtils(pr);
	}

    @Override
	@JFinalTx
    public void save(SysRole role) {
        role.setCreateTime(new Date());
		role.save();

        //检查权限是否越权
        checkPrems(role);

        //保存角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
    }

    @Override
	@JFinalTx
    public boolean update(SysRole role) {
		boolean updateSucc = role.update();
        //检查权限是否越权
        checkPrems(role);

        //更新角色与菜单关系
        sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
		return updateSucc;
    }

    @Override
    @JFinalTx
    public void deleteBatch(Long[] roleIds) {
        //删除角色
		this.dao.deleteBatch(roleIds);

        //删除角色与菜单关联
        sysRoleMenuService.deleteBatch(roleIds);

        //删除角色与用户关联
        sysUserRoleService.deleteBatch(roleIds);
    }

	@Override
	public List<Long> queryRoleIdList(Long createUserId) {
		SysRole sysRole = new SysRole();
		sysRole.setCreateUserId(createUserId);
		List<SysRole> sysRoleList = this.dao.findByModel(sysRole);
		List<Long> roleIdList = sysRoleList.stream().map(item->{ return item.getRoleId();}).collect(Collectors.toList());
		return roleIdList;
	}

	/**
	 * 检查权限是否越权
	 */
	private void checkPrems(SysRole role){
		//如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
		if(role.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}
		
		//查询用户所拥有的菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(role.getCreateUserId());
		
		//判断是否越权
		if(!menuIdList.containsAll(role.getMenuIdList())){
			throw new RRException("新增角色的权限，已超出你的权限范围");
		}
	}
}

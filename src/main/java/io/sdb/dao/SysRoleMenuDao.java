package io.sdb.dao;

import io.sdb.model.SysRoleMenu;
import org.springframework.stereotype.Component;

@Component
public class SysRoleMenuDao extends BaseDao<SysRoleMenu> {
    public SysRoleMenuDao() {
        super(SysRoleMenu.class);
    }
}

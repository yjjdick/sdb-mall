package io.sdb.dao;

import io.sdb.model.SysUserRole;
import org.springframework.stereotype.Component;

@Component
public class SysUserRoleDao extends BaseDao<SysUserRole> {
    public SysUserRoleDao() {
        super(SysUserRole.class);
    }
}

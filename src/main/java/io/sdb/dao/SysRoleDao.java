package io.sdb.dao;

import io.sdb.model.SysRole;
import org.springframework.stereotype.Component;

@Component
public class SysRoleDao extends BaseDao<SysRole> {
    public SysRoleDao() {
        super(SysRole.class);
    }
}

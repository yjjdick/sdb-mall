package io.sdb.dao;

import io.sdb.model.SysUserToken;
import org.springframework.stereotype.Component;

@Component
public class SysUserTokenDao extends BaseDao<SysUserToken> {
    public SysUserTokenDao() {
        super(SysUserToken.class);
    }
}

package io.sdb.dao;

import io.sdb.model.SysConfig;
import org.springframework.stereotype.Component;

@Component
public class SysConfigDao extends BaseDao<SysConfig> {
    public SysConfigDao() {
        super(SysConfig.class);
    }
}

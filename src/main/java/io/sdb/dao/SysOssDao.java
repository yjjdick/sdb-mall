package io.sdb.dao;

import io.sdb.model.SysOss;
import org.springframework.stereotype.Component;

@Component
public class SysOssDao extends BaseDao<SysOss> {
    public SysOssDao() {
        super(SysOss.class);
    }
}

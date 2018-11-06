package io.sdb.dao;

import io.sdb.model.SysLog;
import org.springframework.stereotype.Component;

@Component
public class SysLogDao extends BaseDao<SysLog> {
    public SysLogDao() {
        super(SysLog.class);
    }
}

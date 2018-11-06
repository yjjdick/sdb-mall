package io.sdb.dao;

import io.sdb.model.ScheduleJobLog;
import org.springframework.stereotype.Component;

@Component
public class ScheduleJobLogDao extends BaseDao<ScheduleJobLog> {
    public ScheduleJobLogDao() {
        super(ScheduleJobLog.class);
    }
}

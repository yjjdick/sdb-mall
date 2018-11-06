package io.sdb.dao;

import io.sdb.model.ScheduleJob;
import org.springframework.stereotype.Component;

@Component
public class ScheduleJobDao extends BaseDao<ScheduleJob> {
    public ScheduleJobDao() {
        super(ScheduleJob.class);
    }
}

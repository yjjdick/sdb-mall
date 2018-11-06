package io.sdb.dao;

import io.sdb.dao.BaseDao;
import io.sdb.model.Logistics;
import org.springframework.stereotype.Component;

@Component
public class LogisticsDao extends BaseDao<Logistics> {
    public LogisticsDao() {
        super(Logistics.class);
    }
}
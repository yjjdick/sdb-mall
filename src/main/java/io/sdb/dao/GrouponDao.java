package io.sdb.dao;

import io.sdb.dao.BaseDao;
import io.sdb.model.Groupon;
import org.springframework.stereotype.Component;

@Component
public class GrouponDao extends BaseDao<Groupon> {
    public GrouponDao() {
        super(Groupon.class);
    }
}
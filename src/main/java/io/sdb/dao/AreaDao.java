package io.sdb.dao;

import io.sdb.model.Area;
import org.springframework.stereotype.Component;

@Component
public class AreaDao extends BaseDao<Area> {
    public AreaDao() {
        super(Area.class);
    }
}

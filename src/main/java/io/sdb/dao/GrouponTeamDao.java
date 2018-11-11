package io.sdb.dao;

import io.sdb.dao.BaseDao;
import io.sdb.model.GrouponTeam;
import org.springframework.stereotype.Component;

@Component
public class GrouponTeamDao extends BaseDao<GrouponTeam> {
    public GrouponTeamDao() {
        super(GrouponTeam.class);
    }
}
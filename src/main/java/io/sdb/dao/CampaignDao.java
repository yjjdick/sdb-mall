package io.sdb.dao;

import io.sdb.dao.BaseDao;
import io.sdb.model.Campaign;
import org.springframework.stereotype.Component;

@Component
public class CampaignDao extends BaseDao<Campaign> {
    public CampaignDao() {
        super(Campaign.class);
    }
}
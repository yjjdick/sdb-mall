package io.sdb.service.impl;

import io.sdb.dao.CampaignDao;
import io.sdb.model.Campaign;
import io.sdb.service.CampaignService;
import org.springframework.stereotype.Service;

@Service
public class CampaignServiceImpl extends BaseServiceImpl<CampaignDao, Campaign> implements CampaignService {
}
package io.sdb.service.impl;

import io.sdb.dao.GrouponDao;
import io.sdb.model.Groupon;
import io.sdb.service.GrouponService;
import org.springframework.stereotype.Service;

@Service
public class GrouponServiceImpl extends BaseServiceImpl<GrouponDao, Groupon> implements GrouponService {
}
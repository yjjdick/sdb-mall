package io.sdb.service.impl;

import io.sdb.dao.SnDao;
import io.sdb.enums.SnEnum;
import io.sdb.model.Sn;
import io.sdb.service.SnService;
import org.springframework.stereotype.Service;


@Service
public class SnServiceImpl extends BaseServiceImpl<SnDao, Sn> implements SnService {

    @Override
    public String generate(SnEnum type) {
        return this.dao.generate(type);
    }
}

package io.sdb.dao;

import io.sdb.model.Goods;
import org.springframework.stereotype.Component;

@Component
public class GoodsDao extends BaseDao<Goods> {
    public GoodsDao() {
        super(Goods.class);
    }
}

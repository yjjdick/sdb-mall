package io.sdb.dao;

import io.sdb.dao.BaseDao;
import io.sdb.model.FavoriteGoods;
import org.springframework.stereotype.Component;

@Component
public class FavoriteGoodsDao extends BaseDao<FavoriteGoods> {
    public FavoriteGoodsDao() {
        super(FavoriteGoods.class);
    }
}
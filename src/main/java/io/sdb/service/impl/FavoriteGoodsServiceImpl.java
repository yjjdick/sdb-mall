package io.sdb.service.impl;

import io.sdb.dao.FavoriteGoodsDao;
import io.sdb.model.FavoriteGoods;
import io.sdb.service.FavoriteGoodsService;
import org.springframework.stereotype.Service;

@Service
public class FavoriteGoodsServiceImpl extends BaseServiceImpl<FavoriteGoodsDao, FavoriteGoods> implements FavoriteGoodsService {
}
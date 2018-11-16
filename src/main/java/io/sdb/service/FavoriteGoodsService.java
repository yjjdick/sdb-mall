package io.sdb.service;

import io.sdb.dto.FavoriteGoodsDTO;
import io.sdb.model.FavoriteGoods;

import java.util.List;

/**
 * 收藏商品
 */
public interface FavoriteGoodsService extends BaseService<FavoriteGoods> {
    List<FavoriteGoodsDTO> list(String userId);
}
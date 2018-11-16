package io.sdb.service.impl;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import io.sdb.common.utils.RecordUtils;
import io.sdb.dao.FavoriteGoodsDao;
import io.sdb.dto.FavoriteGoodsDTO;
import io.sdb.model.FavoriteGoods;
import io.sdb.service.FavoriteGoodsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteGoodsServiceImpl extends BaseServiceImpl<FavoriteGoodsDao, FavoriteGoods> implements FavoriteGoodsService {

    @Override
    public List<FavoriteGoodsDTO> list(String userId) {
        SqlPara sqlPara =  Db.getSqlPara("favoriteGoods.list", Kv.by("userId", userId));
        List<Record> recordList = Db.find(sqlPara);
        List<FavoriteGoodsDTO> favoriteGoodsDTOList = RecordUtils.converModel(recordList, FavoriteGoodsDTO.class);
        return favoriteGoodsDTOList;
    }
}
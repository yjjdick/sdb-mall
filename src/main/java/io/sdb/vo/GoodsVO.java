package io.sdb.vo;

import io.sdb.model.Goods;
import io.sdb.model.base.BaseGoods;
import lombok.Data;

@Data
public class GoodsVO extends Goods {
    Boolean favorite;
}

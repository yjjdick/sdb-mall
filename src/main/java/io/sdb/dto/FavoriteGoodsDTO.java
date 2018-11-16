package io.sdb.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FavoriteGoodsDTO {
    /** 商品Id. */
    String goodsId;

    BigDecimal price;

    String name;

    String caption;

    String specificationValues;

    String image;
}

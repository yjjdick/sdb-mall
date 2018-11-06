package io.sdb.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车
 * Created by yjjdick
 * 2017-06-11 19:37
 */
@Data
public class CartDTO {
    Integer id;

    /** 商品Id. */
    String productId;

    /** 数量. */
    Integer quantity;

    BigDecimal price;

    String name;

    String caption;

    String specificationValues;

    String image;

    public CartDTO() {
    }

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.quantity = productQuantity;
    }
}
